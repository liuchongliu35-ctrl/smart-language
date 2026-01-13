package com.bing.websocket.pictureRecognition;

import com.bing.websocket.soundSynthesis.SoundThread;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PictureThread extends Thread{
    private static final Logger log = LoggerFactory.getLogger(SoundThread.class);
    private WebSocketClient webSocketClient;
    private String requestObject;

    public PictureThread() {
    }

    public PictureThread(WebSocketClient webSocketClient, String requestObject) {
        this.webSocketClient = webSocketClient;
        this.requestObject = requestObject;
    }

    @Override
    public void run() {
        PictureWebsocket pictureWebsocket = new PictureWebsocket();
        if(webSocketClient!=null&&requestObject!=null){
            try {
                webSocketClient.send(requestObject);
                while (!pictureWebsocket.getWsCloseFlag()){
                    Thread.sleep(100);
                }
                webSocketClient.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else {
            log.error("请求参数为空，无法建立websocket连接！");
        }
    }
}
