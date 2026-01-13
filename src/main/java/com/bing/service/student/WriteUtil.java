package com.bing.service.student;

import java.util.List;

public interface WriteUtil {
    /**
     * 处理作文批改结果
     */
    public List<String> dealWriteResult(Integer uid,String text);
}
