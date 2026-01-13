package com.bing.controller.student;

import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bing.VO.ClampVo;
import com.bing.VO.StatusVO;
import com.bing.VO.WordVo;
import com.bing.bean.Result;
import com.bing.bean.ResultCodeEnum;
import com.bing.bean.WordClamp;
import com.bing.bean.WordList;
import com.bing.mapper.WordMapper;
import com.bing.service.student.WordBook;
import com.graphbuilder.org.apache.harmony.awt.gl.Crossing;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("wordBook")
public class WordBookController<T> {
    @Resource
    private Result<T> result;

    @Resource
    private WordBook wordBook;

    @Resource
    private WordMapper wordMapper;


    /**
     * 新建模块：给单词本新建情景
     *
     * 单词本操作：添加情景，涉及插入操作
     * 在原来有情景的情况下添加情景
     */
    @PostMapping("addClamp")
    public Result addNewClamp(@RequestBody ClampVo clampVo) {
        int i = wordBook.addNewClamp(clampVo.getUid(),clampVo.getType(),clampVo.getKind());
        if (i != 1) return result.fail(null, "情景添加失败");
        return result.success("情景添加成功");
    }

    /**
     * 单词本管理模块：删除指定情景
     *
     * 在原来有情景的情况下删除情景
     */
    @DeleteMapping("deleteClamp")
    public Result deleteClamp(@Param(value = "uid") Integer uid, @Param(value = "type") String type) {
        int i = wordBook.deleteClampById(uid, type);
        if (i != 1) return result.fail(null, "情景删除失败！");
        return result.success("删除成功！");
    }
    /**
     * 单词本管理模块：获取指定情景的单词数据
     *
     * 涉及查询操作，根据情景查询单词
     * 根据uid和要查看的情景类型获取该情景下的所有单词
     */
    @GetMapping("words")
    public Result<List<WordVo>> getAllWordByType(@Param(value = "uid") Integer uid, @Param(value = "type") String type) {
    String  code = null;
        List<WordVo> wordFromBook = wordBook.getWordFromBook(uid, type);
//        统计已掌握的单词
        int done=0;
        for(WordVo wordVo:wordFromBook){
            if(wordVo.getState()==1){
                done++;
            }
        }
        if(wordFromBook.get(0).getGrammar()!=null){
//            大于20表明已拓展
           if(wordFromBook.size()>20){
//               判断新生成的单词的第一个有没有语法，没有就返回505，让页面等待
               if (wordFromBook.get(wordFromBook.size()-10).getGrammar()==null)
                   code="505";
               else code="504";
           }else
               code="504";
        }else {
            code="505";
        }
        System.out.println("ai生成的单词数量为："+wordFromBook.size());
        return result.build(wordFromBook,code,String.valueOf(done));
    }


/**
 * 单词本管理模块：修改单词掌握情况
 *
 * 更新单词的掌握状态，涉及更新操作
 * 更新单词的掌握状态
 */
@PostMapping("status")
    public Result<String> updateWordStatus(@RequestBody StatusVO statusVO){
    int i = wordBook.updateWordStatus(statusVO.getCid(), statusVO.getText(), statusVO.getState());
    if(i!=1) return result.fail(null,"掌握状态更新失败！");
    return result.success("更新成功！");
}

    /**
     * 单词本管理模块：获取一个单词本的所有情景

     * 根据用户id查询单词本的所有情景，涉及查询操作
     * @param uid
     * @return
     */

@GetMapping("getClamp")
    public Result<List<WordClamp>> getClamp(@Param(value = "uid") Integer uid){
    Random random=new Random();
//    获取uid下的所以clamp
    List<WordClamp> clampByUid = wordBook.getClampByUid(uid);
    List<Integer> cids=new ArrayList<>();
    for (WordClamp w:clampByUid){
        cids.add(w.getClampId());
        w.setRandom(random.nextInt(14));
    }
//    统计每一个情景的的单词数量
//    QueryWrapper<WordList> queryWrapper=new QueryWrapper<>();
//    queryWrapper.in("cid",cids).select("cid");
//    List<WordList> wordLists = wordMapper.selectList(queryWrapper);
//    Map<Integer, List<WordList>> collect = wordLists.stream().collect(Collectors.groupingBy(WordList::getCid));
//    Integer mostCommonCid = null;
//    int maxCount = -1;
//    for (Map.Entry<Integer, List<WordList>> entry : collect.entrySet()) {
//        int currentCount = entry.getValue().size();
//        if (currentCount > maxCount) {
//            maxCount = currentCount;
//            mostCommonCid = entry.getKey();
//        }
//    }
    // 输出结果
//    System.out.println("cid最多的组对应的cid是: " + mostCommonCid);
//    System.out.println("最多的是："+maxCount);
    if(clampByUid==null) return result.fail(null,"情景获取失败！");
    return result.success(clampByUid);
}

//    @GetMapping("grammar")
//    public Result<List<String>> getGrammar(@Param(value = "word") String word){
////        调用大模型
//        List<String> grammar = wordBook.grammarFromModel(word);
//        if(grammar==null) return result.fail(null,"无法从大模型获取数据，请联系管理员");
//        return result.success(grammar);
//    }


/**
 * 单词本管理模块：删除指定单词
 *
 * 根据单词id，对单词进行删除操作
 */
    @DeleteMapping("deleteWid")
    public Result<T> deleteByWordId(@Param("wordId") Integer wordId){
        int byId = wordMapper.deleteById(wordId);
//        int delete = wordMapper.deleteByWordId(wordId);
        if (byId != 1) return result.fail(null, "删除失败");
        return result.build(null, ResultCodeEnum.SUCCESS);
    }


/**
 * 单词本管理模块：修改指定单词
 *
 * 根据单词id，对单词进行修改操作
 */

    @PutMapping("modifyByWid")
    public Result<String> modifyByWid(@RequestBody WordVo wordVo){
        int update=0;
        StrBuilder grammarStr=new StrBuilder();
//        修改语法、文本、翻译、发音
        LambdaUpdateWrapper<WordList> wrapper = new LambdaUpdateWrapper<>();
        if (wordVo.getWordId()!=null){
            if(wordVo.getText()!=null){
                wrapper.set(WordList::getText,wordVo.getText());
            }
            if (wordVo.getMotherText()!=null){
                wrapper.set(WordList::getMotherText,wordVo.getMotherText());
            }
            if (wordVo.getGrammar()!=null){
                for(String grammar:wordVo.getGrammar()){
                    grammarStr.append(grammar).append("\n");
                }
                wrapper.set(WordList::getGrammer,grammarStr.toString());
            }
            if (wordVo.getSpell()!=null){
                wrapper.set(WordList::getSpell,wordVo.getSpell());
            }
            wrapper.eq(WordList::getWordId,wordVo.getWordId());
            update = wordMapper.update(null, wrapper);
        }
        System.out.println(update);
        if (update!=1) return result.fail(null, "修改失败");
        return result.build(null, ResultCodeEnum.SUCCESS);
    }

}
