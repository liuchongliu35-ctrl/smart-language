package com.bing.service.XHmodelUse.XHImpl;

import com.bing.bean.Result;
import com.bing.bean.RoleContent;
import com.bing.service.XHmodelUse.IWrite;
import com.bing.websocket.xfModel.XFWebSocketClient;
import com.bing.websocket.xfModel.XFWebSocketClientListener;
import jakarta.annotation.Resource;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 向大模型请求作文批改结果
 * @param <T>
 */
@Component
public class IWriteImpl<T> implements IWrite {
    @Resource
    private Result<T> result;
    private static final Logger log= LoggerFactory.getLogger(DialogWithModelImpl.class);
    @Override
    public Result WriteCorrection(Integer uid, String text) {
        RoleContent roleContent = new RoleContent();
        RoleContent user = roleContent.createUser(text);
        List<RoleContent> qu=new ArrayList<>();
        qu.add(user);
        XFWebSocketClientListener listener = new XFWebSocketClientListener();
        XFWebSocketClient xfWebSocketClient = new XFWebSocketClient();
//        websocket的执行和本方法中其他代码的执行是异步的，所以后面需要一个while循环+sleep来等待结果
//        AnswerHandler answerHandler = new AnswerHandler();
//        listener.addObserver(answerHandler);
        WebSocket webSocket = xfWebSocketClient.sendMsg(String.valueOf(uid), qu, listener);
        if(webSocket==null){
            log.error("无法连接大模型！");
            return null;
        }
        try {
            int min=0;
            while (true){
                if(min>=150||listener.isWsClose()){
                    break;
                }
                min++;
                Thread.sleep(100);
            }
            if(min>150){
                log.error("响应超时！");
                return result.fail(null,"响应超时！");
            }
            return result.success(listener.getReplay());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            webSocket.close(1000,"");
        }
        return result.success(null);
    }
}
