package com.bing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

//单词表
@Data
public class WordList {
    @TableId(value = "word_id", type = IdType.AUTO)
    private Integer wordId;
//    cid表示单词归属哪一个情景单词薄
    @TableField("cid")
    private Integer cid;
    @TableField("text")
    private String text;
    @TableField("mother_text")
    private String motherText;
//    语法
    @TableField("grammer")
    private String grammer;
//    音标
    @TableField("spell")
    private String spell;
//    该单词的学习状态，0表示难点未学会，1表示已学会
    @TableField("state")
    private Integer state;
//    该单词的难度，分为入门，进阶，提升
    @TableField("difficulty")
    private String difficulty;
//  单词的语音路径
    @TableField("mp3_path")
    private String mp3Path;
}
