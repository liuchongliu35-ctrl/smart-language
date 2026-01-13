package com.bing.controller.student;

import cn.hutool.core.lang.copier.SrcToDestCopier;
import com.bing.VO.ClampVo;
import com.bing.bean.Result;
import com.bing.service.student.StudentService;
import com.bing.service.student.WordBook;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**该controller用来初始化用户的数据，如选择的语言类型，初始化50个基础单词等等，只要用户点击确定语言类型后就会执行以上功能
 *
 */
@CrossOrigin
@RequestMapping("init")
@RestController
public class InitUserInfoController<T> {
    @Resource
    private WordBook wordBook;

    @Resource
    private Result<T> result;

    @Resource
    private StudentService service;
    private static final Logger log= LoggerFactory.getLogger(InitUserInfoController.class);
    /**
     * 用户模块：新建用户，给用户新建单词本
     *
     * 涉及增添用户信息、单词本操作
     * 初始化用户表
     */
    @PostMapping("initUser")
    public Result<Integer> initUser(@Param(value = "lang") String lang,@Param(value = "userName") String userName){
        int book=-1;
        int i = service.iniUser(userName, lang);
        if(i!=-1) {
             book = wordBook.createBook(i, lang);
        } else {
            return result.fail(null,"用户初始化失败！");
        }
        if (book==-1){
            log.error("单词本初始化失败！");
            return result.fail(null,"单词本创建失败");
        }
        return result.success(1);
    }

//    /**
//     * 初始化单词本，给单词本添加情景
//     */
//    @PostMapping("initClamp")
//    public Result initWordBook(@RequestBody ClampVo clampVo){
//        int i = wordBook.initClamp(clampVo.getUid(), clampVo.getType(),clampVo.getKind());
//        if(i!=1) return result.fail(null,"情景初始化失败！");
//        return result.success(1);
//    }
    /**
     * 拓展模块：给单词拓展语音
     *
     * 涉及更新、查询操作
     * 先查询对应的单词，看有没有音频，如果没有，就将新的音频路径更新到相应的单词元组的队友属性中
     *
     * 初始化情景并给情景添加20个单词后，就初始化每个情景的所有单词音频
     * 也可以在添加了单词情景后给新的单词添加音频
     */
    @PostMapping("updateSound")
    public Result updateSound(@Param(value = "uid") Integer uid){
        int i = wordBook.initWordSound(uid);
        if(i!=1) return result.fail(null,"更新单词音频出错！");
        return result.success("音频库更新成功");
    }


}
