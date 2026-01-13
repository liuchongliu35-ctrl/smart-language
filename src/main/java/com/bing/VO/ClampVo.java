package com.bing.VO;

import lombok.Data;

import java.util.List;


@Data
public class ClampVo {
    private Integer uid;
    private List<String> type;
    private String kind;
}
