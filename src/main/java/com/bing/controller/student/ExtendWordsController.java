package com.bing.controller.student;


import com.bing.VO.WordListVo;
import com.bing.bean.Result;
import com.bing.service.student.WordBook;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;


/**
 * 包括使用大模型添加单词和自定义添加单词
 */
@CrossOrigin
@RestController
@RequestMapping("extend")
public class ExtendWordsController<T> {

    @Resource
    private Result<T> result;

    @Resource
    private WordBook wordBook;

    /**
     * 拓展模块：拓展单词
     * 拓展单词：涉及查询，添加操作
     * 根据cid给一个情景拓展20个单词，向大模型发请求
     */
    @PostMapping("words")
    public Result<T> wordFromModel(@Param(value = "cid") Integer cid) throws InterruptedException {
        if(cid==null){
            return result.fail(null,"cid不为空！");
        }
        wordBook.extendWords(cid);
        return result.build(null,null,"单词添加成功!");
    }

    /**
     * 新建模块：给情景新添加单词
     * 涉及增添操作
     * 根据type和uid给一个情景添加新的单词
     */
    @PostMapping("addWord")
    public Result<String> addNewWord(@RequestBody WordListVo wordListVo) {
        int insert = wordBook.addCustomWord(wordListVo);
        if (insert != 1) return result.fail(null, "单词添加失败");
        return result.success("单词添加成功！");
        }


}
