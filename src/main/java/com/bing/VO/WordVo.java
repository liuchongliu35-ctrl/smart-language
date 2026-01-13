package com.bing.VO;

import lombok.Data;

import java.util.List;

@Data
public class WordVo {
    private Integer wordId;
    private Integer cid;
    private String text;
    private String motherText;
    private List<String> grammar;
    private String spell;
    private String mp3Url;
    private Integer state;
    private String  lang;
}
