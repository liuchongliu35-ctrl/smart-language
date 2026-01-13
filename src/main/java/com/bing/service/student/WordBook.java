package com.bing.service.student;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bing.VO.WordListVo;
import com.bing.VO.WordVo;
import com.bing.bean.WordClamp;
import com.bing.bean.WordList;

import java.util.List;

public interface WordBook extends IService<WordList> {

    /**
     * 用于初始化用户单词本
     * @param uid
     * @param language
     * @return
     */
    public int createBook(Integer uid, String language);

    /**
     * 用于从单词本中获取相应情景的单词
     *
     * @param uid
     * @return
     */
    public List<WordVo> getWordFromBook(Integer uid,String type);


    /**
     * 向情景表中添加用户选择的情景
     * @param uid
     * @param type
     * @return
     */
//    public int initClamp(Integer uid,List<String> type,String kind);

    /**
     * 该方法用于徐昂单词本里自定义添加单词
     */
    public int addCustomWord(WordListVo wordListVo);

    /**
     * 初始化单词的音频
     */
    public int initWordSound(Integer uid);


    /**
     * 给单词本里的情景做删除操作
     */
    public int deleteClampById(Integer uid,String type);

    /**
     * 给单词本里的情景做修改操作（实际上是添加新的情景）
     */
    public int  addNewClamp(Integer uid,List<String> type,String kind);


    /**
     * 修改单词的掌握状态
     */
    public int updateWordStatus(Integer cid,String word,Integer status);

    /**
     * 根据uid获取clamp
     */

    public List<WordClamp> getClampByUid(Integer uid);

    /**
     * 拓展20个单词
     */
    public void extendWords(Integer cid) throws InterruptedException;


}
