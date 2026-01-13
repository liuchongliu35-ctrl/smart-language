package com.bing.websocket.soundSynthesis;

import com.bing.Main;
import com.bing.bean.SoundJsonParse;
import com.bing.common.ResourceService;
import com.bing.common.ResourceType;
import com.bing.utils.WaveHeaderUtils;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SoundWebsocket  {
    // 地址与鉴权信息
    public static final String hostUrl = "https://tts-api.xfyun.cn/v2/tts";
    // 均到控制台-语音合成页面获取
    public static final String appid = "6987ab2c";
    public static final String apiSecret = "ZTllMGJjMTljY2M3YjkwMjg3ZmJjZjlh";
    public static final String apiKey = "9103eb8cfe17fea12e1867df3afdcabf";
    @Autowired
    private ResourceService  resource;
//    以下三个参数可以通过调用函数的地方获取
    //    // 合成文本
//    public static final String TEXT = "안녕하세요";
//    // 合成文本编码格式
//    public static final String TTE = "UNICODE"; // 小语种必须使用UNICODE编码作为值
//    // 发音人参数。到控制台-我的应用-语音合成-添加试用或购买发音人，添加后即显示该发音人参数值，若试用未添加的发音人会报错11200
//    public static final String VCN = "xiaoyan";

    // 合成文件名称    H:\EducationPlatform\soundSynthesis\src\main\resources\sounds

    public  String getPath() throws IOException {
        return  resource.getResourcePath(ResourceType.PCM,"").toString();//todo 先构建根路径
//        src/main/resources/MP3/1720788147023.mp3
//        src/main/resources/MP3/1719820829339.mp3
//        src\main\resources\MP3\1723609924823.mp3
    }
//    获取项目的更路径
    public static String OUTPUT_FILE_PATH ="";
    public String MP3Path="";
    public String getMP3Path(){
        return MP3Path;
    }
    // json
    public static final Gson gson = new Gson();
    public boolean wsCloseFlag = false;
    private static final Logger log = LoggerFactory.getLogger(SoundWebsocket.class);
    public boolean getWsCloseFlag(){
       return this.wsCloseFlag;
   }
    public int connect(String texts, String vcn,String tte) throws Exception  {
        String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        OUTPUT_FILE_PATH=Paths.get(getPath(),System.currentTimeMillis()+".pcm").toString();//在构建pcm文件
        OutputStream outputStream = new FileOutputStream(OUTPUT_FILE_PATH);
        String requestObject = getParams(texts, vcn, tte);
        if (requestObject == null) {
            log.error("解析请求参数出错!");
            return 0;
        }

        try {
            URI uri = new URI(url);
            WebSocketClient webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("wss连接创建成功...");
                }

                @Override
                public void onMessage(String text) {
                    SoundJsonParse jsonParse = gson.fromJson(text, SoundJsonParse.class);
                    if(jsonParse.getCode()!=0){
                        log.error("请求发生错误，错误码为："+jsonParse.getCode());
                        log.error("本次请求的sid为"+jsonParse.getSid());
                    }
                    if(jsonParse.getData()!=null){
                        byte[] data = Base64.getDecoder().decode(jsonParse.getData().getAudio());
                        try {
                            outputStream.write(data);
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(jsonParse.getData().getStatus()==2){
                            try {
                                outputStream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("本次请求的sid==>" + jsonParse.getSid());
                            System.out.println("合成成功，文件保存路径为==>" + OUTPUT_FILE_PATH);
//                            MP3Path="H:\\EduPlatForm\\src\\main\\resources\\MP3\\"+System.currentTimeMillis() +".mp3";
//                            String relativePath="src/main/resources/MP3";
                            try {
                                String rootPath = resource.getResourcePath(ResourceType.MP3, "").toString();
                                MP3Path = Paths.get(rootPath,System.currentTimeMillis()+".mp3").toString();
//                                System.currentTimeMillis() + ".mp3"
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            File mp3=new File(MP3Path);
                            System.out.println(MP3Path);
                            File pcm=new File(OUTPUT_FILE_PATH);
                            try {
                                String convertAudioFiles = convertAudioFiles(pcm, mp3);
                                if(convertAudioFiles.equals("ok")){
                                    log.info("pcm格式已成功转化为MP3格式");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            wsCloseFlag=true;
                        }
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("ws链接已关闭，本次请求完成...");
                }

                @Override
                public void onError(Exception e) {
                    log.error("websocket请求发生错误，错误信息:" + e.getMessage());
                }
            };
            webSocketClient.connect();
            wsCloseFlag=false;
            while (!webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
                System.out.println("正在建立连接...");
                Thread.sleep(100);
            }
            SoundThread soundThread=new SoundThread(webSocketClient,requestObject);
            soundThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

//将pcm文件转换为MP3格式
public static String convertAudioFiles(File pcm, File mp3) throws IOException {
    FileInputStream fis = new FileInputStream(pcm);
    FileOutputStream fos = new FileOutputStream(mp3);

    //计算长度
    byte[] buf = new byte[1024 * 4];
    int size = fis.read(buf);
    int PCMSize = 0;
    while (size != -1) {
        PCMSize += size;
        size = fis.read(buf);
    }
    fis.close();

    //填入参数，比特率等等。这里用的是16位单声道 8000 hz
    WaveHeaderUtils header = new WaveHeaderUtils();
    //长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
    header.fileLength = PCMSize + (44 - 8);
    header.FmtHdrLeth = 16;
    header.BitsPerSample = 16;
    header.Channels = 1;
    header.FormatTag = 0x0001;
    header.SamplesPerSec = 16000;//正常速度是8000，这里写成了16000，速度加快一倍
    header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
    header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
    header.DataHdrLeth = PCMSize;
//将数字类型的变为二进制写入文件的步骤：
//        1.将每一个数据的二进制拆分到一个byte数组中，将byte数组写入ByteArrayOutputStream中
//        2.将ByteArrayOutputStream转为byte数组在写入目标文件
    byte[] h = header.getHeader();

    assert h.length == 44; //WAV标准，头部应该是44字节
    //write header
    fos.write(h, 0, h.length);
    //write data stream
    fis = new FileInputStream(pcm);
    size = fis.read(buf);
    while (size != -1) {
        fos.write(buf, 0, size);
        size = fis.read(buf);
    }
    fis.close();
    fos.close();
    System.out.println("PCM Convert to MP3 OK!");
    return "ok";
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

    public String getParams(String texts, String vcn, String tte) {
        String requestObject;
        String encodeText;
//        判断：如果传过来的是小语种(小语种的tte是UNICODE)就使用UTF_16LE编码，不然用UTF_8编码
        if (!texts.equals("") && !tte.equals("") && !vcn.equals("")) {
            if(tte.equals("UNICODE")){
                encodeText = Base64.getEncoder().encodeToString(texts.getBytes(StandardCharsets.UTF_16LE));
            }else {
                encodeText=Base64.getEncoder().encodeToString(texts.getBytes(StandardCharsets.UTF_8));
            }
            requestObject = "{\n" +
                    "  \"common\": {\n" +
                    "    \"app_id\": \"" + appid + "\"\n" +
                    "  },\n" +
                    "  \"business\": {\n" +
                    "    \"aue\": \"raw\",\n" +
                    "    \"tte\": \"" + tte + "\",\n" +
                    "    \"ent\": \"intp65\",\n" +
                    "    \"vcn\": \"" + vcn + "\",\n" +
                    "    \"pitch\": 50,\n" +
                    "    \"speed\": 50\n" +
                    "  },\n" +
                    "  \"data\": {\n" +
                    "    \"status\": 2,\n" +
                    "    \"text\": \"" + Base64.getEncoder().encodeToString(texts.getBytes(StandardCharsets.UTF_16LE)) + "\"\n" +
//                        "    \"text\": \"" + Base64.getEncoder().encodeToString(TEXT.getBytes(StandardCharsets.UTF_16LE)) + "\"\n" +
                    "  }\n" +
                    "}";
            return requestObject;
        }else {
            log.error("文本、cv、编码参数为空或有误！");
        }
        return null;
    }
}
