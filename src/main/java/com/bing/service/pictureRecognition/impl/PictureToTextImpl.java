package com.bing.service.pictureRecognition.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bing.bean.ocrResult.JsonParse;
import com.bing.service.pictureRecognition.PictureToText;
import com.bing.websocket.pictureRecognition.OCRHttpSend;
import com.bing.websocket.pictureRecognition.PictureWebsocket;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class PictureToTextImpl implements PictureToText {
    private static final Logger log= LoggerFactory.getLogger(PictureToTextImpl.class);
    private static String text;
    private static PictureWebsocket pictureWebsocket;
    private static Gson json = new Gson();

//  这种方式只能识别中英文
    @Override
    public String photoToText(String photoPath) {
        pictureWebsocket=new PictureWebsocket();
        int i = pictureWebsocket.sendPicture(photoPath);
        if(i==0){
            return null;
        }else {
            try {
                while (pictureWebsocket.getText().equals("")){
                    Thread.sleep(100);
                }
                text=pictureWebsocket.getText();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }

//    这种方式可以识别52种图片中的文字
    @Override
    public String photoToTextWithMoreLanguage(String photoPath){
        OCRHttpSend send = new OCRHttpSend();
        String resultText = "";
        try {
            String resp = send.doRequest(photoPath);
//            字符串转为json对象，在将json中的包含识别文字的key提取出来
            JsonParse myJsonParse = json.fromJson(resp, JsonParse.class);
            String textBase64Decode=new String(Base64.getDecoder().decode(myJsonParse.payload.recognizeDocumentRes.text), "UTF-8");
            JSONObject jsonObject = JSON.parseObject(textBase64Decode);
            resultText=jsonObject.get("whole_text").toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return resultText;
    }
}
