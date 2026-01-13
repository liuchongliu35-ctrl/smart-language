package com.bing.bean;

import lombok.Data;

/**
 * 用于封装向大模型发请求是的请求头的参数
 */
@Data
public class Header {
    private Integer code;
    private Integer status;
    private String sid;
}
