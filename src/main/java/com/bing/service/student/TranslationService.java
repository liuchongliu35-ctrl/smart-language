package com.bing.service.student;

public interface TranslationService {

    /**
     * 将音频转翻译
     */
    public String audioTranslation(String audioPath,String language);
}
