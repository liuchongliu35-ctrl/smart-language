package com.bing.service.XHmodelUse.XHImpl;

import com.bing.bean.Result;
import com.bing.bean.RoleContent;
import com.bing.service.XHmodelUse.DialogWithModel;
import com.bing.websocket.xfModel.DialogueClient;
import com.bing.websocket.xfModel.DialogueListener;
import okhttp3.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DialogWithModelImpl implements DialogWithModel {

    private static final Logger log= LoggerFactory.getLogger(DialogWithModelImpl.class);
    /**
     * 该方法用于接受客户端发来的问题以及相应的大模型角色要求
     * @param uid
     * @param text
     * @param systemRole
     * @return
     */
    @Override
    public Result dialogToModel(String uid, String text, String systemRole) {
        Result result = new Result();
//        准备一个text集合
//        RoleContent system = new RoleContent();
//        RoleContent role = system.createSystem(systemRole);
        RoleContent system = new RoleContent().createSystem(systemRole);
//        RoleContent user = new RoleContent();
//        user.createUser(text);
        RoleContent user = new RoleContent().createUser(text);
        List<RoleContent> questions = new ArrayList<>();
        questions.add(system);
        questions.add(user);
        //        调用发送消息给大模型的方法  XFWebsocketClient
//        XFWebSocketClientListener listener = new XFWebSocketClientListener();
        DialogueListener listener=new DialogueListener();
//        XFWebSocketClient xfWebSocketClient = new XFWebSocketClient();
        DialogueClient dialogueClient=new DialogueClient();
//        websocket的执行和本方法中其他代码的执行是异步的，所以后面需要一个while循环+sleep来等待结果
        WebSocket webSocket = dialogueClient.sendMsg(uid, questions, listener);
        if (webSocket == null) {
            log.info("大模型访问失败！");
            return result.fail(null, "无法访问大模型，请联系管理员！");
        }
        try {
            int flag = 0;
            while (flag <= 180) {
                Thread.sleep(100);
                if (listener.isWsClose()) {
                    break;
                }
                flag++;
            }
            if (flag > 180) {
                log.error("响应超时！");
                return result.fail(null, "响应超时，请联系管理员！");
            }
            System.out.println("返回给客户端的信息：" + listener.getReplay());

            return result.success(listener.getReplay());
        } catch (Exception e) {
            log.error("请求异常：{ " + e + " }");
        } finally {
//            关闭websocket的状态码1000，原因为空字符串
            webSocket.close(1000, "");
        }
        return result.success("0");

    }
}
