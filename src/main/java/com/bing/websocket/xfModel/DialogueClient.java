package com.bing.websocket.xfModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bing.bean.RoleContent;
import com.bing.config.XFConfig;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DialogueClient {
    public static final String hostUrl = "https://spark-api.xf-yun.com/v3.5/chat";
    public static final String appid = "6987ab2c";
    public static final String apiSecret = "ZTllMGJjMTljY2M3YjkwMjg3ZmJjZjlh";
    public static final String apiKey = "9103eb8cfe17fea12e1867df3afdcabf";
    private static final Logger log= LoggerFactory.getLogger(XFWebSocketClient.class);
    public WebSocket sendMsg(String uid, List<RoleContent> question, WebSocketListener listener){
//        首先获得鉴权url
        String authUrl=null;
        XFConfig xf=new XFConfig();
        try {
            authUrl=getAuthUrl(hostUrl,apiKey,apiSecret);
        } catch (Exception e) {
            log.error("鉴权失败，无法连接大模型！");
            return null;
        }
/**用okhttp3发送websocket请求的一般步骤
 */
//        1.构建可以发送请求的OKHttp实例
        OkHttpClient client=new OkHttpClient.Builder().build();
//        2.将鉴权url里的http请求换成wss请求
        String url=authUrl.toString().replace("http://","ws://").replace("https://","wss://");
//        3.获取request
        Request request = new Request.Builder().url(url).build();
//        4.用okhttpClient获取websocket对象,newWebSocket需要一个request对象和一个websocket监听器对象
        WebSocket webSocket = client.newWebSocket(request, listener);
//        5.组装请求参数
        JSONObject jsonObject = dealParams(uid, question);
//        System.out.println(jsonObject);
//        执行到这一步后，就会自动调用webSocketListener监听器里的OnOpen方法和OnMessage方法，获取返回结果

        System.out.println(url);
        webSocket.send(jsonObject.toString());
        return webSocket;
    }

    //    用于获取鉴权Url
    public String getAuthUrl(String hostUrl,String apiKey,String apiSecret) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        return httpUrl.toString();
    }

    //    用于组装请求参数
    public JSONObject dealParams(String uid, List<RoleContent> question){
//    jsonObject
        JSONObject jsonObject=new JSONObject();
//    header头部组装
        JSONObject header=new JSONObject();
        header.put("app_id",appid);
        header.put("uid",uid);
//       parameter参数组装
        JSONObject parameter=new JSONObject();
        JSONObject chat=new JSONObject();
        chat.put("domain","generalv3.5");
        chat.put("temperature",1);
        chat.put("max_tokens",1000);
        chat.put("top_k",1);
        parameter.put("chat",chat);

//        payLoad组装
        JSONObject payLoad=new JSONObject();
        JSONObject message=new JSONObject();
        JSONArray text= new JSONArray();
        text.addAll(question);
        message.put("text",text);
        payLoad.put("message",message);

//        将header，parameter，payLoad统一放入一个JSONObject中
        jsonObject.put("header",header);
        jsonObject.put("parameter",parameter);
        jsonObject.put("payload",payLoad);
        return jsonObject;
    }
}
