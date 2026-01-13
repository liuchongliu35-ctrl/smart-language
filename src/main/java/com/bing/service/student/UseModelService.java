package com.bing.service.student;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bing.bean.Result;
import com.bing.bean.WordList;
import com.bing.exception.DateBaseException;
import com.bing.mapper.WordMapper;
import com.bing.service.XHmodelUse.GetResult;
import com.bing.service.XHmodelUse.XHImpl.GetResultImpl;
import jakarta.annotation.Resource;
import org.assertj.core.data.MapEntry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Component
public class  UseModelService {
    private static GetResult getResult = new GetResultImpl();

    @Resource
    private WordMapper wordMapper;

    /**
     * 添加情景的时候给每个情景添加20个单词
     * @param uid
     * @param language
     * @param type
     * @return
     */
    public Map<String, String> baseWordFromModel(int uid, String language, String type) {
        Result result = getResult.sendMessageToXFModel(String.valueOf(uid), "生成常见的20个关于" + type + "的基础" + language +
                "单词，结果只要单词，不要生成其他文字，格式为“单词-中文翻译“，结果只要单词和中文翻译");
        return dealData(result);
    }

    /**
     * 给一个情景拓展20个单词,并添加到数据库中
     */
    public void extendWordFromModel(String oldWord,Integer cid,String type,String language) throws InterruptedException{
        CountDownLatch latch=new CountDownLatch(2);

        Thread extend1=new Thread(()->{
            Result result = getResult.sendMessageToXFModel("1", "生成10个不同于" + oldWord + "以上这些单词的关于" + type + "的" + language + "单词，" +
                    "结果只要单词且一定要不同于以上给出的那些单词，不要生成其他文字，格式为“单词-中文翻译“，结果只要单词和中文翻译");
            HashMap<String, String> wordMap = dealData(result);
//        将获取的数据放到数据库中
            try {
                updateDateBase(wordMap,cid);
            } catch (DateBaseException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
        Thread extend2=new Thread(()->{
            Result result = getResult.sendMessageToXFModel("1", "生成10个不同于" + oldWord + "以上这些单词的关于" + type + "的" + language + "单词，" +
                    "结果只要单词且一定要不同于以上给出的那些单词，不要生成其他文字，格式为“单词-中文翻译“，结果只要单词和准确的中文翻译");
            HashMap<String, String> wordMap = dealData(result);
//        将获取的数据放到数据库中
            try {
                updateDateBase(wordMap,cid);
            } catch (DateBaseException e) {
                e.printStackTrace();
            }
            latch.countDown();
        });
    extend1.start();
    extend2.start();
    latch.await();
    }

    private  HashMap<String, String> dealData(Result result){
        HashMap<String, String> wordMap = new HashMap<>();
        String[] split = result.getData().toString().split("\n");
        for (int i=0;i<split.length;i++){
            int index = split[i].indexOf(".");
            String substring = split[i].substring(index+1);
            String[] split1 = substring.split("-");
            wordMap.put(split1[0],split1[1]);
        }
        return wordMap;
    }

    /**
     * 将拓展的单词插入到单词本中
     */
    private void updateDateBase(HashMap<String, String> wordMap,Integer cid) throws DateBaseException {
        for (Map.Entry<String,String> map:wordMap.entrySet()){
            QueryWrapper<WordList> qw=new QueryWrapper<>();
            qw.eq("text",map.getKey());
            boolean exists = wordMapper.exists(qw);
            System.out.println(exists);
            if(!exists){
                WordList wordList = new WordList();
                wordList.setState(0);
                wordList.setText(map.getKey());
                wordList.setMotherText(map.getValue());
                wordList.setCid(cid);
                int insert = wordMapper.insert(wordList);
                if(insert!=1){
                    throw new DateBaseException("单词插入数据库失败！");
                }
            }
        }
    }

    }
/**
 *  GetResultImpl getResult=new GetResultImpl();
 *         Result result = getResult.sendMessageToXFModel("liuc", "生成常见的20个关于交通的英语单词，结果只要单词，不要生成其他文字，格式为“单词：中文翻译”,序号去掉");
 *         String[] split = result.getData().toString().split("\n");
 *         System.out.println(split[1]);
 *         int index = split[1].indexOf(".");
 *         String substring = split[1].substring(index+1);
 *         String[] split1 = substring.split(":");
 *         System.out.println(split1[0]);
 *         System.out.println(split1[1]);
 */
