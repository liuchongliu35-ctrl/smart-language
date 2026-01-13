package com.bing.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bing.VO.TextVo;
import com.bing.bean.NettyGroup;
import com.bing.bean.ResponseData;
import com.bing.bean.Result;
import com.bing.observe.AnswerHandler;
import com.bing.service.XHmodelUse.GetResult;
import com.bing.service.XHmodelUse.XHImpl.GetResultImpl;
import com.bing.service.XHmodelUse.XHImpl.ReadResultImpl;
import com.bing.service.translation.MachineTranslate;
import com.bing.service.translation.translationImpl.MachineTranslationImpl;
import com.bing.websocket.xfModel.XFWebSocketClientListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *  智能阅读模块
 */


@Component
@ChannelHandler.Sharable
public class WriteServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame>  {
    private ReadResultImpl readResult=new ReadResultImpl();
    private MachineTranslate machineTranslate=new MachineTranslationImpl();
    private static String channelId;
    private static String language;
    public String getLanguage(){
        return language;
    }
    public String getChannelId(){
        return channelId;
    }
    private static Logger log= LoggerFactory.getLogger(WriteServerHandler.class);
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded被调用,{}", JSON.toJSONString(ctx));
        //todo 添加校验功能，校验合法后添加到group中
        // 添加到channelGroup 通道组
        NettyGroup.getChannelGroup().add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("服务器收到消息：{}", msg.text());
        // 获取用户ID,关联channel

        JSONObject jsonObject = JSON.parseObject(msg.text());
        channelId = jsonObject.getString("uid");
        NettyGroup.getUserChannelMap().put(channelId, ctx.channel());
        boolean containsKey = NettyGroup.getUserChannelMap().containsKey(channelId);
        if (containsKey){
            String range = jsonObject.getString("range");
            String type = jsonObject.getString("type");
            String to = jsonObject.getString("to");
            if(to.equals("英语")){
                language="en";
            }else if(to.equals("韩语")){
                language="ko";
            }else if(to.equals("法语")){
                language="fr";
            }else if(to.equals("西班牙语")){
                language="es";
            }else if(to.equals("德语")){
                language="de";
            }else if(to.equals("阿拉伯语")){
                language="ar";
            } else if (to.equals("日语")) {
                language="ja";
            }
            String text="请生成一篇关于"+type+"的"+range+"字中文文章，将所有动词、形容词和副词用括号括起来，只生成文章的内容，不要其他文字";
//            调用大模型接口获取数据
             readResult.ReadResultFromModel(channelId,text);

        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        log.info("handlerRemoved被调用,{}", JSON.toJSONString(ctx));
        // 删除通道
        NettyGroup.getChannelGroup().remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        log.info("通道异常：{}", cause.getMessage());
        // 删除通道
        NettyGroup.getChannelGroup().remove(ctx.channel());
//        removeUserId(ctx);
        ctx.close();
    }
}
