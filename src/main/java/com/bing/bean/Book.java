package com.bing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Book {
    @TableId(value = "book_id", type = IdType.AUTO)
    private Integer bookId;
    @TableField("uid")
    private Integer uid;
    @TableField("language")
    private String language;
}
