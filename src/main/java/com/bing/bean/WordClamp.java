package com.bing.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class WordClamp {
    @TableId(value = "clamp_id", type = IdType.AUTO)
    private Integer clampId;
    @TableField("type")
    private String type;
//    单词本的id
    @TableField("bid")
    private Integer bid;
//    情景所属的大类
    @TableField("kind")
    private String kind;
    @TableField("random")
    private Integer random;

}
