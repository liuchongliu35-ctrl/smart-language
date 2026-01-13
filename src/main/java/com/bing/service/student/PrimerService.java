package com.bing.service.student;

import cn.hutool.core.text.replacer.StrReplacer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.VO.PrimerWords;
import com.bing.bean.BasicWord;

import java.util.List;

public interface PrimerService extends IService<BasicWord> {
    /**
     * 根据单词获取该单词的语音路径
     */
    public String getWavePathByWord(String word);

    /**
     * 获取language语言的所有基础音标，偏旁
     */
    public List<PrimerWords> getAllByLanguage(String language);

    /**
     * 判断某个单词的语音是否在数据库中
     * @return
     */
    public String  checkWavePath(String word,Integer cid);

    /**
     * 给一个单词添加音频
     */
    public int addSound(String word,Integer cid,String SoundPath);
}
