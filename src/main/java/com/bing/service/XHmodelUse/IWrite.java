package com.bing.service.XHmodelUse;

import com.bing.bean.Result;
public interface IWrite {
    /**
     * 发送作文给大模型，获取大模型的评分
     */
    public Result WriteCorrection(Integer  uid,String text);
}
