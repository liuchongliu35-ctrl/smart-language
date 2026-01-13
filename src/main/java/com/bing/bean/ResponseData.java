package com.bing.bean;

import lombok.Data;

@Data
public class ResponseData {
    private int code;
    private String message;
    private String sid;
    private TranslationResult data;
}
