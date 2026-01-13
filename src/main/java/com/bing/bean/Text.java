package com.bing.bean;

import lombok.Data;
//封装大模型返回的文本数据
@Data
public class Text {
    private String role;
    private String content;
}
