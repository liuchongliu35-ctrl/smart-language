package com.bing.websocket.xfModel;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bing.bean.JsonParse;
import com.bing.bean.Text;
import com.bing.bean.WordList;
import com.bing.mapper.WordMapper;
import com.bing.service.XHmodelUse.XHImpl.GrammarResultImpl;
import com.bing.service.student.ThreadService;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class DialogueListener extends WebSocketListener {
    private static final Logger log= LoggerFactory.getLogger(DialogueListener.class);
    private boolean wsCloseFlag=false;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(20);
    private StringBuffer replay=new StringBuffer();
    public boolean isWsClose(){
        return wsCloseFlag;
    }
    public String getReplay(){
        return replay.toString();
    }

    @Autowired
    private  WordMapper wordMapper;

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log.info("大模型连接成功！");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        threadPool.execute(()->{
            super.onMessage(webSocket, text);
//        首先将text变为json串的形式，方便获取其中的数据（将每个数据可以单独地拿出来）
            JsonParse answer = JSON.parseObject(text, JsonParse.class);
            log.info("大模型的回复：{ "+JSON.toJSONString(answer)+"}");
            if(answer.getHeader().getCode()!=0){
                log.error("大模型出现问题："+JSON.toJSONString(answer.getHeader()));
                this.replay.append("无法从大模型获取数据，请联系管理员！");
                this.wsCloseFlag=true;
//            return跳出方法
                return;
            }
            List<Text> texts = answer.getPayLoad().getChoices().getText();
            for(Text text1:texts){
                this.replay.append(text1.getContent());
            }
            if(answer.getHeader().getStatus()==2){
                this.wsCloseFlag=true;
                log.info("将大模型的回答"+answer+"记入历史记录中");
            }
        });
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        log.info("与大模型的连接关闭"+wsCloseFlag);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if(response!=null){
                int code=response.code();
                log.error("onFail body:{}",response.body().string());
                if(code!=101){
                    log.error("星火大模型连接异常");
                }
            }
        } catch (Exception e) {
            log.error("IO异常: {}",e);
        }
    }
}

/**
 * import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.stereotype.Service;
 *
 * @Service
 * public class UserService {
 *     @Autowired
 *     private UserMapper userMapper;
 *
 *     public User getUserById(Long id) {
 *         return userMapper.selectById(id);
 *     }
 *
 *     public boolean updateUserName(Long id, String newName) {
 *         User user = new User();
 *         user.setId(id);
 *         user.setName(newName);
 *         return userMapper.updateById(user) > 0; // 返回更新行数 > 0 表示成功
 *     }
 * }
 *
 *
 *  Integer wordId = ThreadService.wordId;
 *                 if(wordId!=null){
 *                     UpdateWrapper<WordList> uw=new UpdateWrapper<>();
 *                     uw.eq("word_id",wordId);
 * //      将新的内容追加到原来的数据中，如果原来的grammer字段是null，就会返回一个空字符串，然后将新的内容加入更新
 *                     uw.setSql("grammer = CONCAT(IFNULL(grammer, ''), '" + text1.getContent() + "')");
 *                     wordMapper.update(null,uw);
 *                 }
 */