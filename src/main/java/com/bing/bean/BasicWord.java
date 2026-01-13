package com.bing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
//语言学习初步：音标学习
@Data
public class BasicWord {
    @TableId(value = "yid", type = IdType.AUTO)
    private Integer yid;
    @TableField("symbol")
    private String symbol;
    @TableField("pronunciation")
    private String pronunciation;
    @TableField("language")
    private String language;
    @TableField("example")
    private String example;
    @TableField("translation")
    private String translation;
    @TableField("type")
    private Integer type;
    @TableField("wave_path")
    private String wavePath;
}
