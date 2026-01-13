package com.bing.observe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bing.bean.JsonParse;
import com.bing.bean.NettyGroup;
import com.bing.bean.Text;
import com.bing.netty.handler.WriteServerHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AnswerHandler implements AnswerObserver{
    private static final Logger log= LoggerFactory.getLogger(AnswerHandler.class);
    private WriteServerHandler writeServerHandler=new WriteServerHandler();
    @Override
    public void onAnswerReceived(JsonParse answer) {
        System.out.println("监听的信息："+answer.getPayLoad().getChoices().getText());
        String shortResult = JSON.toJSONString(answer.getPayLoad().getChoices().getText(), SerializerFeature.PrettyFormat);
        ConcurrentHashMap<String, Channel> map = NettyGroup.getUserChannelMap();
        for(String channelKey:map.keySet()){
            if(channelKey.equals(writeServerHandler.getChannelId())){
                Channel channel = map.get(channelKey);
                if(channel!=null){
//                   准备返回参数
                    List<Text> text = answer.getPayLoad().getChoices().getText();
                    channel.writeAndFlush(new TextWebSocketFrame(shortResult));
                    log.info("信息发送成功！");
                }else {
                    log.error("消息发送失败，该用户使用的通道为空！");
                }
            }else {
                log.error("未找到uid用户对应的通道！");
            }
        }
    }
}
