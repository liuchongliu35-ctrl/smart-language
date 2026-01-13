package com.bing.service.student.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.VO.PrimerWords;
import com.bing.bean.BasicWord;
import com.bing.bean.Book;
import com.bing.bean.WordClamp;
import com.bing.bean.WordList;
import com.bing.mapper.BasicWordMapper;
import com.bing.mapper.BookMapper;
import com.bing.mapper.WordClampMapper;
import com.bing.mapper.WordMapper;
import com.bing.service.student.PrimerService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 该service是用来处理语言入门业务的，主要是对语言基础知识的学习以及入门引导
 */
@Service
public class PrimerServiceImpl extends ServiceImpl<BasicWordMapper, BasicWord> implements PrimerService {

    @Autowired
    private BasicWordMapper basicWordMapper;
    @Autowired
    private WordMapper wordMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private WordClampMapper wordClampMapper;
    private static final Logger log= LoggerFactory.getLogger(PrimerServiceImpl.class);
    @Override
    public String getWavePathByWord(String word) {
        QueryWrapper<BasicWord> qw=new QueryWrapper();
        qw.eq("symbol",word);
        BasicWord basicWord = basicWordMapper.selectOne(qw);
        return basicWord.getWavePath();
    }

    @Override
    public List<PrimerWords> getAllByLanguage(String language) {
        QueryWrapper<BasicWord> qw=new QueryWrapper();
        qw.like("language",language);
        List<BasicWord> basicWords = basicWordMapper.selectList(qw);
        List<PrimerWords> primerWords=new ArrayList<>();
        for(BasicWord b:basicWords){
            PrimerWords words = new PrimerWords();
            words.setSymbol(b.getSymbol());
            words.setPronunciation(b.getPronunciation());
            words.setExample(b.getExample());
            words.setTranslation(b.getTranslation());
//            组装音频访问路径
            words.setWaveUrl("http://localhost:8089/student/mp3?word="+b.getSymbol());
            primerWords.add(words);

        }
        return primerWords;
    }

    @Override
    public String checkWavePath(String word,Integer cid) {
            QueryWrapper <WordList> qw1=new QueryWrapper<WordList>();
//            要通过单词和cid来找
            qw1.eq("text",word);
            qw1.eq("cid",cid);
            WordList wordLists = wordMapper.selectOne(qw1);
            if(wordLists==null){
                log.error("无法查找该单词");
                throw  new NullPointerException("未找到该单词！");
            }
            return wordLists.getMp3Path();
    }

    @Override
    public int addSound(String word,Integer cid,String SoundPath) {
        QueryWrapper <WordList> qw1=new QueryWrapper<WordList>();
//            要通过单词和cid来找
        qw1.eq("text",word);
        qw1.eq("cid",cid);
        WordList wordLists = wordMapper.selectOne(qw1);
        wordLists.setMp3Path(SoundPath);
        return wordMapper.update(wordLists,qw1);
    }


}
