package com.bing.VO;

import lombok.Data;
//封装学习音标，偏旁时返回前端的对象
@Data
public class PrimerWords {
    private String symbol;
    private String pronunciation;
    private String example;
    private String translation;
//    单词音频的地址
    private String waveUrl;
}