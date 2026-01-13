package com.bing.service.XHmodelUse;


import com.bing.bean.Result;

public interface DialogWithModel {
    public Result dialogToModel(String uid,String text,String systemRole);
}
