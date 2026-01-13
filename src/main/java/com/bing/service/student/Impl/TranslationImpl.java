package com.bing.service.student.Impl;

import com.bing.bean.VoiceResult;
import com.bing.service.student.TranslationService;
import com.bing.service.voiceRecognition.VoiceRecognition;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranslationImpl implements TranslationService {


    private static StringBuffer buffer=new StringBuffer();
    @Resource
    private VoiceRecognition voiceRecognition;
    /**
     * 音频翻译
     * @param audioPath
     * @return
     */
    @Override
    public String audioTranslation(String audioPath,String language) {
//        调用语音识别接口
        try {
            List<VoiceResult> voiceResults = voiceRecognition.baseRecognition(audioPath, language);
            for(VoiceResult vr:voiceResults){
                buffer.append(vr.getOnebest());
            }
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
