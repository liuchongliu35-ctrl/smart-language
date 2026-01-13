package com.bing.service.student.Impl;

import com.bing.bean.Result;
import com.bing.service.XHmodelUse.IWrite;
import com.bing.service.student.WriteUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class WriteUtilImpl implements WriteUtil {

    @Resource
    private IWrite iWrite;
    /**
     * 处理作文批改结果
     */
    @Override
    public List<String> dealWriteResult(Integer uid, String text) {
        List<String> resultList=new ArrayList<>();
        text+= "给以上作文评分，分数为百分制，评语请指出具体的错误结果以“1.内容：分数+评语\n" +
                "            2.结构：分数  评语\n" +
                "            3. 语法：分数  评语\n" +
                "            4.语言：分数  评语\n" +
                "            5. 总分：平均分数 ”的形式输出";
        Result result = iWrite.WriteCorrection(uid, text);
        String resultString = result.getData().toString();
        String[] split = resultString.split("\n");
        resultList.addAll(Arrays.asList(split));
        return resultList;
    }
}
