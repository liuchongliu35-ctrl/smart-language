package com.bing.websocket.xfModel;


import com.alibaba.fastjson.JSON;
import com.bing.bean.JsonParse;
import com.bing.bean.NettyGroup;
import com.bing.bean.Text;
import com.bing.netty.handler.WriteServerHandler;
import com.bing.observe.AnswerHandler;
import com.bing.observe.AnswerObserver;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 该类用于监听XFWebSocketClient发送websocket请求到大模型的过程，并获取大模型的返回值
 */

/**
 * ！！！！！重要：调用大模型的无论哪个接口时大多数返回的都是字符串，处理这些字符串的常规操作就是用
 * JsonParse将字符串变为json串，方便获取对应的key和value
 * 
 */
@Component
public class XFWebSocketClientListener extends WebSocketListener {
   private static final Logger log=LoggerFactory.getLogger(XFWebSocketClientListener.class);
//   websocket处于连接状态是为false，true是连接断开
   private boolean wsCloseFlag=false;
   private StringBuffer replay=new StringBuffer();
   public boolean isWsClose(){
       return wsCloseFlag;
   }
   public String getReplay(){
       return replay.toString();
   }


//   创建线程池，每一个websocket都放到一个线程里执行
private static final ExecutorService threadPool = Executors.newFixedThreadPool(12);
   // 创建一个固定大小的线程池

//    private List<AnswerObserver> observers = new ArrayList<>();
//
//    public void addObserver(AnswerObserver observer) {
//        observers.add(observer);
//    }
//
//    public void removeObserver(AnswerObserver observer) {
//        observers.remove(observer);
//    }
//
//    private void notifyObservers(JsonParse answer) {
//        for (AnswerObserver observer : observers) {
//            observer.onAnswerReceived(answer);
//        }
//    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.info("大模型连接成功！");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
//        创建线程池：
        threadPool.execute(()->{
            super.onMessage(webSocket, text);
//        首先将text变为json串的形式，方便获取其中的数据（将每个数据可以单独地拿出来）
            JsonParse answer = JSON.parseObject(text, JsonParse.class);
            log.info("大模型的回复：{ "+JSON.toJSONString(answer)+"}");
            if(answer.getHeader().getCode()!=0){
                log.error("大模型出现问题："+JSON.toJSONString(answer.getHeader()));
                this.replay.append("无法从大模型获取数据，请联系管理员！");
                this.wsCloseFlag=true;
//            return跳出方法
                return;
            }
            List<Text> texts = answer.getPayLoad().getChoices().getText();
            for(Text text1:texts){
//            log.info("返回的信息结果：",JSON.toJSONString(text1));
//            将结果变成string放到StringBuffer中
//            System.out.println("结果"+text1.getContent());
                this.replay.append(text1.getContent());
//            textList.add(text1);
            }
//        System.out.println("大模型的回答："+replay);
            if(answer.getHeader().getStatus()==2){
                this.wsCloseFlag=true;
                log.info("将大模型的回答"+answer+"记入历史记录中");
            }
        });
//        notifyObservers(answer);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        log.info("与大模型的连接关闭"+wsCloseFlag);
    }

    @Override
    public void onFailure(WebSocket webSocket,Throwable t,Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if(response!=null){
                int code=response.code();
                log.error("onFail body:{}",response.body().string());
                if(code!=101){
                log.error("星火大模型连接异常");
                }
            }
        } catch (Exception e) {
            log.error("IO异常: {}",e);
        }
    }
}
