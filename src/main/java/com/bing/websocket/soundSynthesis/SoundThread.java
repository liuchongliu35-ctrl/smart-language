package com.bing.websocket.soundSynthesis;

import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundThread extends Thread{
    private static final Logger log = LoggerFactory.getLogger(SoundThread.class);
    private WebSocketClient webSocketClient;
    private String requestObject;
    public SoundThread(WebSocketClient webSocketClient1,String requestObject){
        this.webSocketClient=webSocketClient1;
        this.requestObject=requestObject;
    }
    @Override
    public void run() {
        SoundWebsocket soundWebsocket=new SoundWebsocket();
        if(webSocketClient!=null&&requestObject!=null){
            try {
                webSocketClient.send(requestObject);
                while (!soundWebsocket.getWsCloseFlag()){
                    Thread.sleep(100);
                }
                webSocketClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            log.error("请求参数为空，无法建立websocket连接！");
        }
    }
}
