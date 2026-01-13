package com.bing.service.XHmodelUse.XHImpl;

import com.bing.bean.RoleContent;
import com.bing.exception.ModelException;
import com.bing.service.XHmodelUse.GrammarResult;
import com.bing.websocket.xfModel.DialogueListener;
import com.bing.websocket.xfModel.XFWebSocketClient;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GrammarResultImpl implements GrammarResult {
    public static WebSocket webSocket;

    private static final Logger log= LoggerFactory.getLogger(GrammarResultImpl.class);
    @Override
    public void GrammarResultFromModel(String uid, String text) {
        RoleContent roleContent=new RoleContent();
        RoleContent user = roleContent.createUser(text);
        List<RoleContent> roleContents=new ArrayList<>();
        roleContents.add(user);
        DialogueListener dialogueListener =new DialogueListener();
        XFWebSocketClient xfWebSocketClient=new XFWebSocketClient();
        webSocket = xfWebSocketClient.sendMsg(uid, roleContents, dialogueListener);

        if (webSocket == null) {
            log.error("websocket连接失败");
            throw new ModelException("大模型错误，获取数据失败");
        }
    }
}
