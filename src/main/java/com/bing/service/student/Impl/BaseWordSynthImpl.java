package com.bing.service.student.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.bean.BasicWord;
import com.bing.mapper.BasicWordMapper;
import com.bing.service.soundSynthesis.XFSoundSynthesis;
import com.bing.service.student.BaseWordSynth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BaseWordSynthImpl extends ServiceImpl<BasicWordMapper, BasicWord> implements BaseWordSynth {
    @Autowired
    private BasicWordMapper basicWordMapper;
    @Autowired
    private XFSoundSynthesis soundSynthesis;
    private static final Logger log= LoggerFactory.getLogger(BaseWordSynthImpl.class);
    private static List<String> soundPaths=new ArrayList<>();
    @Override
    public List<String> englishSoundSynth() {
//    先从数据库中获取所有的英文字母
        QueryWrapper<BasicWord> qw=new QueryWrapper<>();
        qw.select("yid","symbol","language","example","wave_path").eq("language","en");
        List<BasicWord> basicWords = basicWordMapper.selectList(qw);
        QueryWrapper<BasicWord> qw1=new QueryWrapper<BasicWord>();
        for (BasicWord bw:basicWords){
//            只有在没有音频路径的时候才会执行
            if(bw.getWavePath().equals("")&&bw.getLanguage()!=null&&bw.getYid()!=null){
                String text= bw.getSymbol()+"......"+bw.getExample();
//                调用语音合成接口
                String s = soundSynthesis.baseSoundSynthesis(text, bw.getLanguage());
//                将该单词的音频路径给该对象
                if(s.equals("")){
                    continue;
                }
                bw.setWavePath(s);
                int update = basicWordMapper.updateById(bw);
//                更新成功就将路径进行保存
                if(update==1){
                    soundPaths.add(s);
                }
            }else {
                log.info(bw.getSymbol()+"..."+bw.getExample()+"单词的音频文件存在或该单词没有明确语言类型！");
            }
        }
        return soundPaths;
    }

    @Override
    public List<String> japanSoundSynth() {
        List<String> sound=new ArrayList<>();
        QueryWrapper<BasicWord> qw=new QueryWrapper<>();
        qw.select("yid","symbol","language","example","wave_path").like("language","jap");
        List<BasicWord> basicWords = basicWordMapper.selectList(qw);
        QueryWrapper<BasicWord> qw1=new QueryWrapper<BasicWord>();
        for (BasicWord bw:basicWords){
            if(bw.getWavePath().equals("")&&bw.getLanguage()!=null&&bw.getYid()!=null){
                String text= bw.getSymbol()+"......"+bw.getExample();
//                调用语音合成接口
                String s = soundSynthesis.baseSoundSynthesis(text, bw.getLanguage());
//                将该单词的音频路径给该对象
                if(s.equals("")){
                    continue;
                }
                bw.setWavePath(s);
                int update = basicWordMapper.updateById(bw);
//                更新成功就将路径进行保存
                if(update==1){
                    sound.add(s);
                }
            }else {
                log.info(bw.getSymbol()+"..."+bw.getExample()+"单词的音频文件存在或该单词没有明确语言类型！");
            }
        }
        return sound;
    }

    @Override
    public List<String> koreaSoundSynth() {
        List<String> sounds=new ArrayList<>();
        QueryWrapper<BasicWord> qw=new QueryWrapper<>();
        qw.select("yid","symbol","language","example","wave_path").like("language","ko");
        List<BasicWord> basicWords = basicWordMapper.selectList(qw);
        QueryWrapper<BasicWord> qw1=new QueryWrapper<BasicWord>();
        for (BasicWord bw:basicWords){
            if(bw.getWavePath().equals("")&&bw.getLanguage()!=null&&bw.getYid()!=null){
                String text= bw.getSymbol()+"......"+bw.getExample();
//                调用语音合成接口
                String s = soundSynthesis.baseSoundSynthesis(text, bw.getLanguage());
//                将该单词的音频路径给该对象
                bw.setWavePath(s);
                int update = basicWordMapper.updateById(bw);
//                更新成功就将路径进行保存
                if(update==1){
                    sounds.add(s);
                }
            }else {
                log.info(bw.getSymbol()+"..."+bw.getExample()+"单词的音频文件存在或该单词没有明确语言类型！");
            }
        }
        return sounds;
    }
}
