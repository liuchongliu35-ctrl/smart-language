package com.bing.websocket.xfModel;

import cn.hutool.core.text.StrBuilder;
import com.alibaba.fastjson.JSON;
import com.bing.bean.JsonParse;
import com.bing.bean.NettyGroup;
import com.bing.bean.ResponseData;
import com.bing.bean.Text;
import com.bing.exception.ModelException;
import com.bing.netty.handler.WriteServerHandler;
import com.bing.service.XHmodelUse.XHImpl.ReadResultImpl;
import com.bing.service.translation.MachineTranslate;
import com.bing.service.translation.translationImpl.MachineTranslationImpl;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ReadResultListener extends WebSocketListener {
    private WriteServerHandler handler=new WriteServerHandler();
    private static final Logger log= LoggerFactory.getLogger(ReadResultListener.class);
    private boolean wsCloseFlag=false;
    private MachineTranslate machineTranslate=new MachineTranslationImpl();
    private StringBuffer data=new StringBuffer();


    private void sendMsgToClient(String channelId,String data){
        ConcurrentHashMap<String, Channel> map = NettyGroup.getUserChannelMap();
        for (String channelKey : map.keySet()) {
            if (channelKey.equals(channelId)) {
                Channel channel = map.get(channelKey);
                if (channel != null) {
//                   准备返回参数
                    channel.writeAndFlush(new TextWebSocketFrame(data));
                    log.info("信息发送成功！");
                } else {
                    log.error("消息发送失败，该用户使用的通道为空！");
                }
            } else {
                log.error("未找到uid用户对应的通道！");
            }
        }
    }
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.info("大模型连接成功！");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        String channelId = handler.getChannelId();
        String language = handler.getLanguage();
        super.onMessage(webSocket, text);
//        首先将text变为json串的形式，方便获取其中的数据（将每个数据可以单独地拿出来）
        JsonParse answer = JSON.parseObject(text, JsonParse.class);
        log.info("大模型的回复：{ "+JSON.toJSONString(answer)+"}");
        if(answer.getHeader().getCode()!=0){
            log.error("大模型出现问题："+JSON.toJSONString(answer.getHeader()));
            if(channelId!=null)
             sendMsgToClient(channelId,"大模型出现问题："+JSON.toJSONString(answer.getHeader()));
            this.wsCloseFlag=true;
            throw new ModelException("大模型出现问题："+JSON.toJSONString(answer.getHeader()));
        }
        List<Text> texts = answer.getPayLoad().getChoices().getText();
        for(Text text1:texts){
//            log.info("返回的信息结果：",JSON.toJSONString(text1));
//            将结果变成string放到StringBuffer中
//            System.out.println("结果"+text1.getContent());
            if(channelId!=null&&language!=null){
//                先将获取的数据翻译为目标语言
                data.append(text1.getContent());
                ResponseData translationData = machineTranslate.getTranslation("cn", language, text1.getContent());
                sendMsgToClient(channelId,translationData.getData().getResult().getTrans_result().getDst());
            }
//            textList.add(text1);
        }
//        System.out.println("大模型的回答："+replay);
        if(answer.getHeader().getStatus()==2){
            this.wsCloseFlag=true;
            log.info("将大模型的回答"+answer+"记入历史记录中");
            ReadResultImpl.webSocket.close(1000,"");
            sendMsgToClient(channelId,"翻译: "+data.toString());
        }
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        log.info("与大模型的连接关闭"+wsCloseFlag);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
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
