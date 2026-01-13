package com.bing.websocket.pictureRecognition;

import com.bing.bean.PhotoJsonParse;
import com.bing.common.ResourceService;
import com.bing.common.ResourceType;
import com.bing.service.pictureRecognition.impl.PictureToTextImpl;
import com.bing.utils.DocxFileUtil;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class PictureWebsocket {

    // 地址与鉴权信息
    public static final String hostUrl = "https://ws-api.xf-yun.com/v1/private/ma008db16";
    public static final String appid = "6987ab2c";
    public static final String apiSecret = "ZTllMGJjMTljY2M3YjkwMjg3ZmJjZjlh";
    public static final String apiKey = "9103eb8cfe17fea12e1867df3afdcabf";

//    @Autowired
    ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final ResourceService resource=new ResourceService(resourceLoader);

    public static final String RESULT_TYPE = "1";
//    public static final String OUTPUT_FILE_PATH = "H:\\EduPlatForm\\src\\main\\resources\\docxFile\\"+ System.currentTimeMillis() +".docx";
    // json
    public static final Gson gson = new Gson();
    public  boolean wsCloseFlag = false;
    private static final Logger log= LoggerFactory.getLogger(PictureToTextImpl.class);

    public  boolean getWsCloseFlag(){
        return this.wsCloseFlag;
    }

    private String text="";
    public String getText(){
        return this.text;
    }

    public int sendPicture(String picturePath){
        try {
            String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
            String newUrl = authUrl.replace("http://", "ws://").replace("https://", "wss://");

            String requestParam = getParam(picturePath);
            if(requestParam.equals("")){
                log.error("参数解析错误！");
                return 0;
            }

            URI uri=new URI(newUrl);
            WebSocketClient webSocketClient=new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("ws建立连接成功...");
                }

                @Override
                public void onMessage(String s) {
                    // System.out.println(text);
                    PhotoJsonParse jsonParse = gson.fromJson(s, PhotoJsonParse.class);
                    if (jsonParse.getHeader().getCode()!=0) {
                        System.out.println("发生错误，错误码为：" + jsonParse.getHeader().getCode());
                        System.out.println("本次请求的sid为：" + jsonParse.getHeader().getSid());
                    }
                    if (jsonParse.getHeader().getStatus()==1) {
                        try {
//                            String relativePath="src/main/resources/docxFile";
//                            todo 先构建根路径
                            Path rootPath = resource.getResourcePath(ResourceType.WORD, "");
                            System.out.println(rootPath.toString());
                            String outPath = Paths.get(rootPath.toString(),System.currentTimeMillis() + ".docx").toString();
                            OutputStream outputStream = new FileOutputStream(outPath);
                            byte[] textBase64Decode = Base64.getDecoder().decode(jsonParse.getPayload().getResult().getText());
                            outputStream.write(textBase64Decode);
                            outputStream.close();
                            System.out.println("表格识别成功，文件保存路径为==>" + outPath);
//                            使用docx工具类获得docx文件的内容
                            DocxFileUtil docxFileUtil = new DocxFileUtil(outPath);
                            text= docxFileUtil.dealDocx();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("本次请求的sid==>" + jsonParse.getHeader().getSid());
                    } else if (jsonParse.getHeader().getStatus() == 2) {
                        // 可以关闭连接，释放资源
                        wsCloseFlag = true;
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    System.out.println("ws链接已关闭，本次请求完成..."+s);
                }

                @Override
                public void onError(Exception e) {
                    log.error("本次请求出错："+e.getMessage());
                }
            };

            webSocketClient.connect();
            while (!webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
                log.info("正在连接...");
                Thread.sleep(100);
            }
            PictureThread pictureThread = new PictureThread(webSocketClient, requestParam);
            pictureThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    return 1;
    }
    /**
     * 现将图片文件变为byte数组，
     * 在将byte数组用 Base64进行编码
     * @param filePath
     * @return
     * @throws IOException
     */
    // 读取文件数据
    public static byte[] read(String filePath) throws IOException {
        InputStream in = new FileInputStream(filePath);
        byte[] data = inputStream2ByteArray(in);
        in.close();
        return data;
    }

    private static byte[] inputStream2ByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    // 鉴权方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        //System.out.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
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


    public String getParam(String picturePath){
        String requestJson="";
        try {
            requestJson = "{\n" +
                    "  \"header\": {\n" +
                    "    \"app_id\": \"" + appid + "\",\n" +
                    "    \"status\":2\n" +
                    "  },\n" +
                    "  \"parameter\": {\n" +
                    "    \"s15282f39\": {\n" +
                    "      \"category\": \"ch_en_public_cloud\",\n" +
                    "      \"result\": {\n" +
                    "        \"encoding\": \"utf8\",\n" +
                    "        \"compress\": \"raw\",\n" +
                    "        \"format\": \"plain\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    \"s5eac762f\": {\n" +
                    "      \"result_type\": \"" + RESULT_TYPE + "\",\n" +
                    "      \"result\": {\n" +
                    "        \"encoding\": \"utf8\",\n" +
                    "        \"compress\": \"raw\",\n" +
                    "        \"format\": \"plain\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"payload\": {\n" +
                    "    \"test\": {\n" +
                    "      \"encoding\": \"" + picturePath.substring(picturePath.length() - 3) + "\",\n" +
                    "      \"image\": \"" + Base64.getEncoder().encodeToString(read(picturePath)) + "\",\n" +
                    "      \"status\": 3\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestJson;
    }

}
