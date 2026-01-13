package com.bing.bean;


import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于将每个客户端接入netty服务器所产生的channel通道放入channelMap中，
 * 并将用户名和相应的channel通道对应，方便通过用户名找到相应的channel并将信息发送至指定客户端
 */
public class NettyGroup {
    private static ChannelGroup channelGroup=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static ConcurrentHashMap<String, Channel> concurrentHashMap=new ConcurrentHashMap<>();
    private NettyGroup(){

    }
    public static ChannelGroup getChannelGroup(){
        return channelGroup;
    }
    public static ConcurrentHashMap<String,Channel> getUserChannelMap(){
        return concurrentHashMap;
    }
}
