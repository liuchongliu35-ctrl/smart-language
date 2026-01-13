package com.bing.service.XHmodelUse.XHImpl;

import com.bing.bean.RoleContent;
import com.bing.exception.ModelException;
import com.bing.service.XHmodelUse.ReadResult;
import com.bing.websocket.xfModel.ReadResultListener;
import com.bing.websocket.xfModel.XFWebSocketClient;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReadResultImpl implements ReadResult {
    private static final Logger log= LoggerFactory.getLogger(ReadResultImpl.class);
    public static WebSocket webSocket;
    @Override
    public void ReadResultFromModel(String uid, String text) {
        RoleContent roleContent=new RoleContent();
        RoleContent user = roleContent.createUser(text);
        List<RoleContent> rc=new ArrayList<>();
        rc.add(user);
        ReadResultListener readResultListener=new ReadResultListener();
        XFWebSocketClient xfWebSocketClient = new XFWebSocketClient();
         webSocket = xfWebSocketClient.sendMsg(String.valueOf(uid), rc, readResultListener);
        if(webSocket==null){
            log.error("无法连接大模型！");
            throw new ModelException("大模型错误，获取数据失败");
        }
    }
}
