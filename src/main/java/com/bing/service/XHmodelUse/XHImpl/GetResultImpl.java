package com.bing.service.XHmodelUse.XHImpl;

import com.alibaba.fastjson.JSON;
import com.bing.bean.NettyGroup;
import com.bing.bean.Result;
import com.bing.bean.RoleContent;
import com.bing.config.XFConfig;
import com.bing.observe.AnswerHandler;
import com.bing.service.XHmodelUse.GetResult;
import com.bing.websocket.xfModel.XFWebSocketClient;
import com.bing.websocket.xfModel.XFWebSocketClientListener;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class GetResultImpl implements GetResult {

    private static final Logger log = LoggerFactory.getLogger(GetResultImpl.class);

    @Override
    public void pushResultToOneUser(String uid, String text) {
        if (uid.equals("") || text.equals("")) {
            log.info("用户名和返回结果不能为空！");
            throw new RuntimeException("uid和text不能为空！");
        }
        /**
         * 找到发送请求的用户：根据用户id和channel关联表来找
         */
        ConcurrentHashMap<String, Channel> map = NettyGroup.getUserChannelMap();
        for (String channelKey : map.keySet()) {
            if (channelKey.equals(uid)) {
                Channel channel = map.get(channelKey);
                if (channel != null) {
//                   准备返回参数
                    channel.writeAndFlush(new TextWebSocketFrame(text));
                    log.info("信息发送成功！");
                } else {
                    log.error("消息发送失败，该用户使用的通道为空！");
                }
            } else {
                log.error("未找到uid用户对应的通道！");
            }
        }
    }

    //该处使用synchronized对象锁，一次只允许一个线程访问该资源
    @Override
    public Result sendMessageToXFModel(String uid, String text) {
        Result result = new Result();
        XFConfig xf = new XFConfig();
//        创建一个角色，每个角色有id和对应的问题（content）
        RoleContent roleContent = new RoleContent();
        RoleContent roleContent1 = roleContent.createUser(text);
        ArrayList<RoleContent> question = new ArrayList<>();
        question.add(roleContent1);
//        调用发送消息给大模型的方法  XFWebsocketClient
        XFWebSocketClientListener listener = new XFWebSocketClientListener();
        XFWebSocketClient xfWebSocketClient = new XFWebSocketClient();
//        websocket的执行和本方法中其他代码的执行是异步的，所以后面需要一个while循环+sleep来等待结果
//        只有请求中有文章字眼的才会添加监视
//        if(text.contains("文章")){
//            AnswerHandler answerHandler = new AnswerHandler();
//            listener.addObserver(answerHandler);
//        }
        WebSocket webSocket = xfWebSocketClient.sendMsg(uid, question, listener);
        if (webSocket == null) {
            log.error("websocket连接失败");
            return result.fail(null, "无法访问大模型，请联系管理员！");
        }
        try {
            int count = 0;
            int maxTime = xf.getMaxResponseTime() * 100;
            while (count <= maxTime) {
                Thread.sleep(100);
                if (listener.isWsClose()) {
                    break;
                }
                count++;
            }
            if (count > maxTime) {
                log.error("响应超时！");
                return result.fail(null, "响应超时，请联系管理员！");
            }
            System.out.println("返回给客户端的信息：" +listener.getReplay());

            return result.success(listener.getReplay());
        } catch (Exception e) {
           log.error("请求异常：{ "+e+" }");
        } finally {
//            关闭websocket的状态码1000，原因为空字符串
            webSocket.close(1000,"");
        }
        return result.success("0");

    }
}
