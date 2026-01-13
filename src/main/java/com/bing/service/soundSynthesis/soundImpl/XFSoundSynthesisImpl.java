package com.bing.service.soundSynthesis.soundImpl;


import com.bing.service.soundSynthesis.XFSoundSynthesis;
import com.bing.websocket.soundSynthesis.SoundWebsocket;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

//讯飞语言合成调用接口
@Component
public class XFSoundSynthesisImpl implements XFSoundSynthesis {
    @Resource
    private SoundWebsocket soundWebsocket;

    private static final Logger log = LoggerFactory.getLogger(XFSoundSynthesisImpl.class);

    /**
     * 完成普通的语音合成，即不需要对结果做过多处理的方法
     *
     * @param texts
     * @param language
     * @return
     */
    @Override
    public String baseSoundSynthesis(String texts, String language) {
        if (!texts.equals("") && !language.equals("")) {
            if (language.equals("日语")) {
                String vcn="x4_jajp_zhongcun_assist";
                String tte="UNICODE";
                String mp3Path = sendParam(texts, vcn, tte);
                return mp3Path;
            }

            if(language.equals("英语")){
                String vcn="x4_enus_lucy_education";
                String tte="UTF8";
                String mp3Path = sendParam(texts, vcn, tte);
                return mp3Path;
            }
            if(language.equals("韩语")){
                String vcn="x2_KoKr_Miya";
                String tte="UNICODE";
                String mp3Path = sendParam(texts, vcn, tte);
                return mp3Path;
            }
            if(language.equals("法语")){
                String vcn="x2_FrRgM_Lisa";
                String tte="UTF8";
                String mp3Path = sendParam(texts, vcn, tte);
                return mp3Path;
            }
            if(language.equals("阿拉伯语")){
                String vcn="x2_ArEn_Rania";
                String tte="UTF8";
                String mp3Path = sendParam(texts, vcn, tte);
                return mp3Path;
            }
            if(language.equals("泰语")){
                String vcn="yingying";
                String tte="UNICODE";
                String mp3Path = sendParam(texts, vcn, tte);
                return mp3Path;
            }
            if(language.equals("西班牙语")){
                String vcn="gabriela";
                String tte="UTF8";
                String mp3Path = sendParam(texts, vcn, tte);
                return mp3Path;
            }
        }
        return null;
    }
    public  String sendParam(String text,String vcn,String tte){
        int i=0;
        try {
            int flag = soundWebsocket.connect(text, vcn, tte);
            Thread.sleep(200);
            while (!soundWebsocket.getWsCloseFlag()){
                if(i>=50){
                   return null;
                }
                System.out.println("正在获取MP3文件的路径...");
                Thread.sleep(100);
                i++;
            }
            if (soundWebsocket.getMP3Path().equals("")) {
                log.info("未获取合成语音文件的路径！");
                throw new RuntimeException("未获得结果路径，无法载入数据库！");
            }
            return soundWebsocket.getMP3Path();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
