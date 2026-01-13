package com.bing.Thread;

import com.bing.bean.Result;
import com.bing.bean.RoleContent;
import com.bing.config.XFConfig;
import com.bing.service.XHmodelUse.GetResult;
import com.bing.service.XHmodelUse.XHImpl.GetResultImpl;
import com.bing.websocket.xfModel.XFWebSocketClient;
import com.bing.websocket.xfModel.XFWebSocketClientListener;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class InitWordThread implements Runnable{
    private Integer uid;
    private String text;
    private String type;

    public InitWordThread(Integer uid, String language, String type) {
        this.uid = uid;
        this.type = type;
    }
private static final Logger log= LoggerFactory.getLogger(InitWordThread.class);
    private static GetResult getResult = new GetResultImpl();
    @Override
    public void run() {


    }
    }

