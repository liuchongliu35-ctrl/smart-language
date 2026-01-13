package com.bing.service.student.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bing.VO.WordListVo;
import com.bing.VO.WordVo;
import com.bing.bean.Book;
import com.bing.bean.Result;
import com.bing.bean.WordClamp;
import com.bing.bean.WordList;
import com.bing.mapper.BookMapper;
import com.bing.mapper.WordClampMapper;
import com.bing.mapper.WordMapper;
import com.bing.service.XHmodelUse.GetResult;
import com.bing.service.student.ThreadService;
import com.bing.service.student.UseModelService;
import com.bing.service.student.WordBook;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class WordBookImpl extends ServiceImpl<WordMapper, WordList> implements WordBook {

    @Autowired
    private WordMapper wordMapper;

    @Autowired
    private WordClampMapper wordClampMapper;

    @Autowired
    private ThreadService threadService;

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private GetResult getResult;

    @Resource
    private UseModelService useModel;
    private static final Logger log = LoggerFactory.getLogger(WordBookImpl.class);

    /**
     * 给用户添加单词本
     * @param uid
     * @param language
     * @return
     */
    @Override
    public int createBook(Integer uid, String language) {
        int insert = 0;
        if (uid != null && !language.equals("")) {
            Book book = new Book();
            book.setUid(uid);
            book.setLanguage(language);
            insert = bookMapper.insert(book);
        }
        return insert;
    }


    /**
     * 向单词表中添加自定义单词
     *
     * @param wordListVo
     * @return
     */
    @Override
    public int addCustomWord(WordListVo wordListVo) {
        WordList wordList = new WordList();
        if (wordListVo.getText() != null && wordListVo.getMotherText() != null  && wordListVo.getCid()!=null) {
            wordList.setText(wordListVo.getText());
            wordList.setMotherText(wordListVo.getMotherText());
            wordList.setCid(wordListVo.getCid());
//            获取单词语法
            String request = "写出意思为"+wordListVo.getMotherText()+"的单词" + wordListVo.getText() + "的词性，词义，以及两个例句以“1.词性： 2.词义： 3.例句： “,最后写出他所有的变形及其词义，每个变形一个例句,所有例句用该单词所属的语种写";
            Result result = getResult.sendMessageToXFModel("7", request);
            String grammar = result.getData().toString();
            wordList.setGrammer(grammar);
//            0表示未学会
            wordList.setState(0);
            wordList.setDifficulty("进阶/高级");
        }else {
            return 0;
        }
        return wordMapper.insert(wordList);
    }

    @Override
    public int initWordSound(Integer uid) {
        List<Integer> listId = new ArrayList<>();
        QueryWrapper<Book> qw = new QueryWrapper<>();
        qw.eq("uid", uid);
        Book book = bookMapper.selectOne(qw);
        QueryWrapper<WordClamp> qw1 = new QueryWrapper<>();
        qw1.select("clamp_id").eq("bid", book.getBookId());
        List<WordClamp> wordClamps = wordClampMapper.selectList(qw1);
        for (WordClamp wordClamp : wordClamps) {
            listId.add(wordClamp.getClampId());
        }
        QueryWrapper<WordList> qw2 = new QueryWrapper<>();
        qw2.in("cid", listId);
        List<WordList> wordLists = wordMapper.selectList(qw2);
        threadService.checkAndUpdateWordSound(book, wordLists);
        return 1;
    }


    /**
     * 根据用户的id找到他的单词本，
     * 再根据单词本找到里面type类型的id
     * 再去单词表里查找与id匹配的单词
     * @param uid
     * @return
     */

    @Override
    public List<WordVo> getWordFromBook(Integer uid, String type) {
        List<WordVo> wordVoList = new ArrayList<>();
        Book book = getBook(uid);
        QueryWrapper<WordClamp> qw1 = new QueryWrapper<>();
        qw1.select("clamp_id").eq("bid", book.getBookId()).eq("type", type);
        WordClamp wordClamp = wordClampMapper.selectOne(qw1);
        QueryWrapper<WordList> qw2 = new QueryWrapper<>();
        qw2.in("cid", wordClamp.getClampId());
        List<WordList> wordLists = wordMapper.selectList(qw2);
        List<WordList> updateWord=new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        for (WordList word : wordLists) {
            List<String> returnGrammar=new ArrayList<>();
            WordVo wordVo = new WordVo();
//            第一次20个单词走到这里的时候word.getGrammer()==null&&wordLists.get(0).getGrammer()==null这两个单词都满足
//            在生成这20个
            if(word.getGrammer()==null&&wordLists.get(0).getGrammer()==null){
//                将没有语法的单词放到一个集合中,当第一个已经有语法但这个没有语法的时候说明其他的正在生成就不需要再生成一遍了
                updateWord.add(word);
            }else {
                if(word.getGrammer()!=null){
                    String grammer = word.getGrammer();
                    String[] split = grammer.split("\n");
                    for (int i=0;i<split.length;i++){
                        split[i]=split[i].replaceAll("\\d+\\.", "");
                        returnGrammar.add(split[i]);
                    }
                    wordVo.setGrammar(returnGrammar);
                }
            }
            if(word.getGrammer()==null&&wordLists.get(0).getGrammer()!=null){
                if(wordLists.size()>20){
//                    if(wordLists.get(wordLists.size()-20).getGrammer()==null)
                    updateWord.add(word);
                    updateWord.add(word);
                }
            }
            BeanUtils.copyProperties(word, wordVo,"grammar");
            wordVo.setLang(book.getLanguage());
            wordVo.setWordId(word.getWordId());//获取单词的id，后续可以根据单词的id进行删除修改等操作
//            wordVo.setMp3Url("http://10.33.65.219:8080/student/mp3?word=" + word.getText()+"&cid="+wordClamp.getClampId());
            wordVoList.add(wordVo);
        }
//        updateWord!=null说明有需要更新的单词  updateWord==null时说明没有单词需要更新
        if(updateWord!=null){
            threadService.grammarFromModel(updateWord, wordClamp.getClampId(),book.getLanguage());
        }
        return wordVoList;
    }

    /**
     * 给情景做删除操作
     *
     * @param uid
     * @param type
     * @return
     */
    @Override
    public int deleteClampById(Integer uid, String type) {
        Book book = getBook(uid);
        QueryWrapper<WordClamp> qw = new QueryWrapper<>();
        qw.select("clamp_id").eq("bid", book.getBookId()).eq("type", type);
        WordClamp wordClamp = wordClampMapper.selectOne(qw);
        QueryWrapper<WordList> qw1 = new QueryWrapper<>();
        qw1.eq("cid", wordClamp.getClampId());
//        先将wordList表里的数据删除
        int delete = wordMapper.delete(qw1);
//        再删除wordClamp表里的
        if(delete == 1){
            int delete1 = wordClampMapper.delete(qw);
            if ( delete1 == 1) {
                return 1;
            }
        }
        log.error("删除操作失败！");
        return -1;
    }

    @Override
    public int addNewClamp(Integer uid, List<String> type,String kind) {
        List<WordClamp> wordClampList=new ArrayList<>();
//        先要检验新的情景在原来的单词本里有没有
        Book book = getBook(uid);
        QueryWrapper<WordClamp> qw = new QueryWrapper<>();
        qw.select("type").eq("bid", book.getBookId());
        List<WordClamp> wordClamps = wordClampMapper.selectList(qw);
        StringBuffer stringBuffer = new StringBuffer();
        for (WordClamp w : wordClamps) {
            stringBuffer.append(w.getType()).append(",");
        }
        String typeString = stringBuffer.toString();
        for (String tp : type) {
            if (typeString.contains(tp)||tp.equals("undefined")) {
                log.info(tp + "类型情景已存在，添加失败");
            } else {
                try {
                    WordClamp wordClamp = new WordClamp();
                    wordClamp.setBid(book.getBookId());
                    wordClamp.setType(tp);
                    wordClamp.setKind(kind);
                    int insert = wordClampMapper.insert(wordClamp);
                    wordClampList.add(wordClamp);
                    if (insert != 1) {
                        log.info(tp + "插入失败！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        在给新添加的情景添加单词
        if(wordClampList.size()!=0){
            try {
                threadService.insetWords(book, wordClampList);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            return -1;
        }
        return 1;
    }

    @Override
    public int updateWordStatus(Integer cid, String word,Integer status) {
        if(status==0){
            status=1;
        }else {
            status=0;
        }
//        根据cid和word查找单词
        QueryWrapper<WordList> qw=new QueryWrapper<>();
        qw.eq("cid",cid).eq("text",word);
        WordList wordList = wordMapper.selectOne(qw);
        wordList.setState(status);
        return  wordMapper.update(wordList,qw);
    }

    @Override
    public List<WordClamp> getClampByUid(Integer uid) {
        Book book = getBook(uid);
        QueryWrapper<WordClamp> qw=new QueryWrapper<>();
        qw.eq("bid",book.getBookId());
        return wordClampMapper.selectList(qw);
    }


    public Book getBook(Integer uid) {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        Book book = bookMapper.selectOne(queryWrapper);
        return book;
    }

    @Override
    public void extendWords(Integer cid) throws InterruptedException {
//        根据cid将所有该情景的单词查询出来
        QueryWrapper<WordList> qw=new QueryWrapper<>();
        qw.select("text").eq("cid",cid);
        List<WordList> oldWords = wordMapper.selectList(qw);
//        获取情景的类型
        QueryWrapper<WordClamp> qw1=new QueryWrapper<>();
        qw1.select("type","bid").eq("clamp_id",cid);
        WordClamp wordClamp = wordClampMapper.selectOne(qw1);
//        获取情景的语言
        QueryWrapper<Book> qw3=new QueryWrapper<>();
        qw3.select("language").eq("book_id",wordClamp.getBid());
        Book book = bookMapper.selectOne(qw3);
        StringBuilder words=new StringBuilder();
//        将数据库原有的单词拼接起来
        for (WordList word:oldWords){
            words.append(word.getText()).append(" ");
        }
        useModel.extendWordFromModel(words.toString(),cid,wordClamp.getType(),book.getLanguage());
    }



}
