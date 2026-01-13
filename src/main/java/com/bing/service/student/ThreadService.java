package com.bing.service.student;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bing.bean.Book;
import com.bing.bean.Result;
import com.bing.bean.WordClamp;
import com.bing.bean.WordList;
import com.bing.exception.OutRangeException;
import com.bing.mapper.BookMapper;
import com.bing.mapper.WordClampMapper;
import com.bing.mapper.WordMapper;
import com.bing.service.XHmodelUse.GetResult;
import com.bing.service.XHmodelUse.XHImpl.GrammarResultImpl;
import com.bing.service.soundSynthesis.XFSoundSynthesis;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


@Component
public class ThreadService {
    private static int flag = 1;
    private static String word;
    private static final Logger log = LoggerFactory.getLogger(ThreadService.class);
    @Autowired
    public WordMapper wordMapper;
    @Autowired
    private XFSoundSynthesis xfSoundSynthesis;
    @Resource
    private WordClampMapper wordClampMapper;
    @Resource
    private UseModelService useModel;
    @Autowired
    private GetResult getResult;
    @Autowired
    private GrammarResultImpl grammarResult;
    public static Integer wordId;

    private static final int MAX_THREADS = 20;
    private static final CountDownLatch latch = new CountDownLatch(MAX_THREADS);


    public void checkAndUpdateWordSound(Book book, List<WordList> wordLists) {
        for (WordList words : wordLists) {
//            先判断该单词有没有音频，有音频就跳过
            if (words.getMp3Path() == null && !words.getText().isEmpty()) {
//                调用语音合成接口
                String mp3Path = xfSoundSynthesis.baseSoundSynthesis(words.getText(), book.getLanguage());
                if (!mp3Path.equals("")) {
//                    更新数据库该单词的mp3路径
                    words.setMp3Path(mp3Path);
                    LambdaUpdateWrapper<WordList> luw = new LambdaUpdateWrapper<>();
                    luw.set(WordList::getMp3Path, mp3Path);
                    luw.eq(WordList::getWordId, words.getWordId());
//                    进行更新：
                    int update = wordMapper.update(null, luw);
                } else {
                    log.error("无法获取该单词的语音路径！");
                }
            }
        }

    }

    public String creatOneWordSound(String word, Book book) {
        String mp3Path = xfSoundSynthesis.baseSoundSynthesis(word, book.getLanguage());
        return mp3Path;
    }
//    public int  InitWord(Book book){
//        QueryWrapper<WordClamp> qw = new QueryWrapper<>();
//        qw.select("clamp_id", "type").eq("bid", book.getBookId());
//        List<WordClamp> wordClamps = wordClampMapper.selectList(qw);
//        return insetWords(book, wordClamps);
//
//    }


    @Async("wordInit")
    public void insetWords(Book book, List<WordClamp> wordClamps) throws InterruptedException {
//        根据情景的多少分类，分为添加1,2,3,4个情景，总共4种情况
//        1、添加一个情景
        switch (wordClamps.size()) {
            case 1 -> {
                CountDownLatch latch = new CountDownLatch(1);
                Thread worker = new Thread(() -> {
                    System.out.println("worker线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(0).getType());
                    insertToDateBase(wordMap, wordClamps.get(0));
//                    处理获得的单词：添加音频，
                    // 完成任务后，计数器减1
                    latch.countDown();
                });
                worker.start();
                //   等待所有线程完成
                latch.await();
            }
            case 2 -> {
                CountDownLatch latch1 = new CountDownLatch(2);
                Thread worker1 = new Thread(() -> {
                    System.out.println("worker1线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> words = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(0).getType());
                    insertToDateBase(words, wordClamps.get(0));
                    // 完成任务后，计数器减1
                    latch1.countDown();
                });
                Thread worker2 = new Thread(() -> {
                    System.out.println("worker2线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> words = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(1).getType());
                    insertToDateBase(words, wordClamps.get(1));
                    // 完成任务后，计数器减1
                    latch1.countDown();
                });
                worker1.start();
                worker2.start();
                //     等待所有线程完成
                latch1.await();
            }
            case 3 -> {
                CountDownLatch latch3 = new CountDownLatch(3);
                Thread worker_1 = new Thread(() -> {
                    System.out.println("worker_1线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(0).getType());
                    insertToDateBase(wordMap, wordClamps.get(0));
                    // 完成任务后，计数器减1
                    latch3.countDown();
                });
                Thread worker_2 = new Thread(() -> {
                    System.out.println("worker_2线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(1).getType());
                    insertToDateBase(wordMap, wordClamps.get(1));
                    // 完成任务后，计数器减1
                    latch3.countDown();
                });
                Thread worker_3 = new Thread(() -> {
                    System.out.println("worker_3线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(2).getType());
                    insertToDateBase(wordMap, wordClamps.get(2));
                    // 完成任务后，计数器减1
                    latch3.countDown();
                });
                worker_1.start();
                worker_2.start();
                Thread.sleep(1000);
                worker_3.start();
                //    等待所有线程完成
                latch3.await();
            }
            case 4 -> {
                CountDownLatch latch4 = new CountDownLatch(4);
                Thread workers1 = new Thread(() -> {
                    System.out.println("workers1线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(0).getType());
                    insertToDateBase(wordMap, wordClamps.get(0));
                    // 完成任务后，计数器减1
                    latch4.countDown();
                });
                Thread workers2 = new Thread(() -> {
                    System.out.println("workers2线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(1).getType());
                    insertToDateBase(wordMap, wordClamps.get(1));
                    // 完成任务后，计数器减1
                    latch4.countDown();
                });
                Thread workers3 = new Thread(() -> {
                    System.out.println("workers3线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(2).getType());
                    insertToDateBase(wordMap, wordClamps.get(2));
                    // 完成任务后，计数器减1
                    latch4.countDown();
                });
                Thread workers4 = new Thread(() -> {
                    System.out.println("workers4线程开启了");
                    //   线程一：向大模型发送请求，获取20个关于运动的英语单词
                    Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wordClamps.get(3).getType());
                    insertToDateBase(wordMap, wordClamps.get(3));
                    // 完成任务后，计数器减1
                    latch4.countDown();
                });
                workers1.start();
                workers2.start();
                Thread.sleep(1000);
                workers3.start();
                Thread.sleep(2000);
                workers4.start();
//                等待所有线程完成
                latch4.await();
            }
            default -> throw new OutRangeException("添加的情景数的上限为4个");
        }


//        for (WordClamp wc : wordClamps) {
//            Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(), book.getLanguage(), wc.getType());
//            for (Map.Entry<String, String> map : wordMap.entrySet()) {
//                WordList wordList = new WordList();
//                wordList.setText(map.getKey());
//                wordList.setMotherText(map.getValue());
//                wordList.setState(1);
//                wordList.setCid(wc.getClampId());
////                获取单词的音频
//                String path = creatOneWordSound(map.getKey(), book);
//                wordList.setMp3Path(path);
////                获取单词的语法
////                String grammar = grammarFromModel(map.getKey());
////                wordList.setGrammer(grammar);
//                int insert = wordMapper.insert(wordList);
//                if (insert != 1) {
//                    flag = insert;
//                    log.error("单词更新失败！");
//                    break;
//                }
//            }
//        }
    }

    /**
     * 将单词插入数据库
     *
     * @param words
     * @param wordClamp
     */
    public void insertToDateBase(Map<String, String> words, WordClamp wordClamp) {
        for (Map.Entry<String, String> map : words.entrySet()) {
            WordList wordList = new WordList();
            wordList.setText(map.getKey());
            wordList.setMotherText(map.getValue());
            wordList.setState(0);
            wordList.setCid(wordClamp.getClampId());

//                获取单词的语法
//                String grammar = grammarFromModel(map.getKey());
//                wordList.setGrammer(grammar);
            int insert = wordMapper.insert(wordList);
            if (insert != 1) {
                flag = insert;
                log.error("单词更新失败！");
                break;
            }
        }
    }

    @Async("wordSoundSynthesis")
    public void grammarFromModel(List<WordList> wordLists, int cid, String language) {
//        版本三
        ScheduledExecutorService scheduledExecutorService =
                Executors.newScheduledThreadPool(20);
        for (int i=0;i<wordLists.size();i++){
            final int index = i;
            scheduledExecutorService.schedule(() -> {
                // 获取数据并执行相应任务
                String data = wordLists.get(index).getText();
                String request = "写出" + language + "单词：" + data + "的词性，所有常用的词义，以及两个例句以“1.词性： 2.词义： 3.例句： “,最后写出他所有的变形及其词义，每个变形一个例句,所有例句用" +
                        language + "写";
                Result result = getResult.sendMessageToXFModel("7", request);
                String grammar = result.getData().toString();
            QueryWrapper<WordList> qw = new QueryWrapper<>();
            qw.eq("word_id",wordLists.get(index).getWordId());
            wordLists.get(index).setGrammer(grammar);
            int update = wordMapper.update(wordLists.get(index), qw);
            if (update != 1) log.error("语法更新失败！");
            }, (long) i, TimeUnit.SECONDS);
        }



    }
}

//        for (WordList word : wordLists) {
//            wordId = word.getWordId();
//            String request = "写出" + language + "单词：" + word + "的词性，词义，以及两个例句以“1.词性： 2.词义： 3.例句： “,最后写出他所有的变形及其词义，每个变形一个例句,所有例句用" + language + "写";
////            版本二：
////            grammarResult.GrammarResultFromModel("7", request);
//
//            版本一：
//            Result result = getResult.sendMessageToXFModel("7", request);
//            String grammar = result.getData().toString();
//            QueryWrapper<WordList> qw = new QueryWrapper<>();
//            qw.eq("cid", cid).eq("text", word.getText());
//            word.setGrammer(grammar);
//            int update = wordMapper.update(word, qw);
//            if (update != 1) log.error("语法更新失败！");
//
//        }
/**
 * private static final int MAX_THREADS = 20;
 *     private static final CountDownLatch latch = new CountDownLatch(MAX_THREADS);
 *
 *     public static void main(String[] args) {
 *         ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
 *         executor.scheduleAtFixedRate(() -> {
 *             if (latch.getCount() > 0) {
 *                 new Thread(() -> {
 *                     try {
 *                         System.out.println("线程启动： " + Thread.currentThread().getName());
 *                         // 模拟线程执行任务
 *                         Thread.sleep(1000);
 *                         System.out.println("线程结束： " + Thread.currentThread().getName());
 *                     } catch (InterruptedException e) {
 *                         e.printStackTrace();
 *                     } finally {
 *                         latch.countDown();
 *                     }
 *                 }).start();
 *             } else {
 *                 executor.shutdown();
 *             }
 *         }, 0, 2, TimeUnit.SECONDS);
 *
 *         try {
 *             latch.await();
 *         } catch (InterruptedException e) {
 *             e.printStackTrace();
 *         }
 *         System.out.println("所有线程已启动，主线程退出");
 *     }
 */


/**
 * Map<String, String> wordMap = useModel.baseWordFromModel(book.getUid(),book.getLanguage(), wc.getType());
 * for (Map.Entry<String, String> map : wordMap.entrySet()) {
 * WordList wordList = new WordList();
 * wordList.setText(map.getKey());
 * wordList.setMotherText(map.getValue());
 * wordList.setState(1);
 * wordList.setCid(wc.getClampId());
 * wordMapper.insert(wordList);
 * }
 * <p>
 * <p>
 * for (String wordType:type){
 * QueryWrapper<WordClamp> qw=new QueryWrapper<>();
 * qw.select("clamp_id").eq("type",wordType);
 * WordClamp wordClamp = wordClampMapper.selectOne(qw);
 * <p>
 * Map<String, String>wordMap = useModel.baseWordFromModel(uid, language, wordType);
 * for(Map.Entry<String,String> map:wordMap.entrySet()){
 * WordList wordList = new WordList();
 * wordList.setText(map.getKey());
 * wordList.setMotherText(map.getValue());
 * wordList.setState(0);
 * wordList.setCid(wordClamp.getClampId());
 * }
 * }
 * <p>
 * for (String wordType:type){
 * QueryWrapper<WordClamp> qw=new QueryWrapper<>();
 * qw.select("clamp_id").eq("type",wordType);
 * WordClamp wordClamp = wordClampMapper.selectOne(qw);
 * <p>
 * Map<String, String>wordMap = useModel.baseWordFromModel(uid, language, wordType);
 * for(Map.Entry<String,String> map:wordMap.entrySet()){
 * WordList wordList = new WordList();
 * wordList.setText(map.getKey());
 * wordList.setMotherText(map.getValue());
 * wordList.setState(0);
 * wordList.setCid(wordClamp.getClampId());
 * }
 * }
 * <p>
 * <p>
 * <p>
 * ExecutorService executor = Executors.newFixedThreadPool(type.length);
 * List<Future<Map<String, String>>> futures = new ArrayList<>();
 * <p>
 * for (String wordType : type) {
 * Callable<Map<String, String>> callable = () -> useModel.baseWordFromModel(uid, language, wordType);
 * Future<Map<String, String>> future = executor.submit(callable);
 * futures.add(future);
 * }
 * <p>
 * for (int i = 0; i < type.length; i++) {
 * try {
 * Map<String, String> wordMap = futures.get(i).get();
 * for (Map.Entry<String, String> map : wordMap.entrySet()) {
 * System.out.println(map.getKey()+" "+map.getValue());
 * WordList wordList = new WordList();
 * wordList.setText(map.getKey());
 * wordList.setMotherText(map.getValue());
 * wordList.setState(0);
 * // 设置cid需要根据wordClamp的值进行设置
 * // wordList.setCid(wordClamp.getClampId());
 * }
 * } catch (InterruptedException | ExecutionException e) {
 * e.printStackTrace();
 * }
 * }
 * <p>
 * executor.shutdown();
 * }
 */