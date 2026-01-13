package com.bing.service.translation;

import com.bing.bean.ResponseData;

public interface MachineTranslate {
    public ResponseData getTranslation(String from, String to, String text);
}
