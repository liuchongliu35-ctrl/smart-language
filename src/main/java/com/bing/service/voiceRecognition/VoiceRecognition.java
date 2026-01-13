package com.bing.service.voiceRecognition;

import com.bing.bean.ResultData;
import com.bing.bean.VoiceResult;

import java.util.List;

public interface VoiceRecognition {
    public  List<VoiceResult> baseRecognition(String filePath,String language) throws InterruptedException;
}
