package com.bing.netty;

import com.bing.netty.handler.WriteServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Component
@ChannelHandler.Sharable
public class WriteServerNetty {
    private static final Logger log= LoggerFactory.getLogger(WriteServerNetty.class);
    @Value("${webSocket.netty.port:8087}")
    private int port;
    /**
     * webSocket路径
     */
    @Value("${webSocket.netty.path:/websocket}")
    private String webSocketPath;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    public void soundStart(){
         bossGroup = new NioEventLoopGroup();
         workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new ObjectEncoder());
                            // 以块的方式来写的处理器
                            ch.pipeline().addLast(new ChunkedWriteHandler());
        /*
        说明：
        1、http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合
        2、这就是为什么，当浏览器发送大量数据时，就会发送多次http请求
         */
                            ch.pipeline().addLast(new HttpObjectAggregator(8192));
        /*
        说明：
        1、对应webSocket，它的数据是以帧（frame）的形式传递
        2、浏览器请求时 ws://localhost:58080/xxx 表示请求的uri
        3、核心功能是将http协议升级为ws协议，保持长连接
        */
/**                new WebSocketServerProtocolHandler("/websocket")
 *                天坑！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
 *
 */
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket"));
                            ch.pipeline().addLast(new WriteServerHandler()); // 自定义处理器
                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            log.info("实时阅读线程已连接"+f.channel().localAddress());
            f.channel().closeFuture().sync();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully().sync();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully().sync();
        }
    }

    @PostConstruct()
    public void init() {
        //需要开启一个新的线程来执行netty server 服务器
        new Thread(() -> {
            soundStart();
            log.info("消息推送线程开启！");
        }).start();
    }
}
