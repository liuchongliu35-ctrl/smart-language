package com.bing.service.XHmodelUse;

import com.bing.bean.Result;

public interface GetResult {
    public void  pushResultToOneUser(String uid, String text);
    public Result sendMessageToXFModel(String uid, String text);
}
