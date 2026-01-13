package com.bing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Student {
    @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;
    @TableField("account")
    private String account;
    @TableField("password")
    private String password;
    @TableField("language")
    private String language;
}
