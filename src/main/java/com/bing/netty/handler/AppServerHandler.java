package com.bing.netty.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bing.VO.DialogVO;
import com.bing.bean.NettyGroup;
import com.bing.bean.ResponseData;
import com.bing.bean.Result;
import com.bing.service.XHmodelUse.DialogWithModel;
import com.bing.service.XHmodelUse.GetResult;
import com.bing.service.XHmodelUse.XHImpl.DialogWithModelImpl;
import com.bing.service.soundSynthesis.XFSoundSynthesis;
import com.bing.service.translation.MachineTranslate;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Field;

/**
 * 情景对话模块
 */

@Component
@ChannelHandler.Sharable
public class AppServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger log = LoggerFactory.getLogger(AppServerHandler.class);

    @Autowired
    private GetResult getResult;
    @Autowired
    private Result result;
    @Resource
    private MachineTranslate translate;
    @Resource
    private DialogWithModel dialogWithModel;

    @Resource
    private XFSoundSynthesis soundSynthesis;
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
        System.out.println(jsonObject);
        String channelId = jsonObject.getString("uid");

        // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        //String channelId = CharUtil.generateStr(uid);
        NettyGroup.getUserChannelMap().put(channelId, ctx.channel());
        boolean containsKey = NettyGroup.getUserChannelMap().containsKey(channelId);
        //通道已存在，请求信息返回
        if (containsKey) {
            String language="";
            //接收消息格式{"uid":"123456","text":"中华人民共和国成立时间","system":"角色要求"}
//            {"uid":"","text":"","system":"","from":"","to":""}
            String text = jsonObject.getString("text");
            String system = jsonObject.getString("system");
////            文本使用的语言
            String from = jsonObject.getString("from");
//            返回结果需要的语言
            String to = jsonObject.getString("to");
            System.out.println(from);
            System.out.println(to);
            if(to==null){
                throw new NullPointerException("请求参数不能为空");
            }
            if(to.equals("en")){
            language="英语";
            } else if(to.equals("ja")){
            language="日语";
            }else if(to.equals("ko")){
            language="韩语";
            }else if(to.equals("fr")){
                language="法语";
            }
            ResponseData translation1 = translate.getTranslation(to, "cn", text);
            String dst = translation1.getData().getResult().getTrans_result().getDst();
            dst+="，你需要用中文像人类一样简洁的回答我";
            System.out.println(dst+","+system);
            //请求大模型服务器，获取结果
            Result resultBean = dialogWithModel.dialogToModel(channelId, dst, system);
//            结果是中文
            String data = (String) resultBean.getData();
//            将中文翻译为学习的语言
            ResponseData translation = translate.getTranslation("cn", to, data);
//            获得结果的语音
            String soundPath = soundSynthesis.baseSoundSynthesis(translation.getData().getResult().getTrans_result().getDst(),language);
            String fileName="";
//            将mp3文件的名字返回
            if(soundPath!=null){
                File file=new File(soundPath);
                fileName = file.getName();
            }
//            包装返回的DialogVO;
            DialogVO dialogVO = new DialogVO();
//            中文转为目标语言
            dialogVO.setReplay(translation.getData().getResult().getTrans_result().getDst());
//            中文回答
            dialogVO.setTranslation(data);
            dialogVO.setReplaySoundPath(fileName);
            dialogVO.setType("replay");
            //推送
           getResult.pushResultToOneUser(channelId, JSON.toJSONString(dialogVO, SerializerFeature.PrettyFormat));
        } else {
            ctx.channel().attr(key).setIfAbsent(channelId);
            log.info("连接通道id:{}", channelId);
            // 回复消息
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(result.success(channelId))));
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved被调用,{}", JSON.toJSONString(ctx));
        // 删除通道
        NettyGroup.getChannelGroup().remove(ctx.channel());
//        removeUserId(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("通道异常：{}", cause.getMessage());
        // 删除通道
        NettyGroup.getChannelGroup().remove(ctx.channel());
//        removeUserId(ctx);
        ctx.close();
    }

    private void removeUserId(ChannelHandlerContext ctx) {
        AttributeKey<String> key = AttributeKey.valueOf("userId");
        String userId = ctx.channel().attr(key).get();
        NettyGroup.getUserChannelMap().remove(userId);
    }
}