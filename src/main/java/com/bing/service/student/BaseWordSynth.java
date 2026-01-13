package com.bing.service.student;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.bean.BasicWord;

import java.util.List;

public interface BaseWordSynth extends IService<BasicWord> {
    public List< String> englishSoundSynth();
    public List< String> japanSoundSynth();
    public List< String> koreaSoundSynth();
}
