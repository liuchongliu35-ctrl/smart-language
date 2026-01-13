package com.bing.bean;

public enum ResultCodeEnum {
    SUCCESS("0","success"),
    FAIL("001","操作失败，请重新操作"),
    NOAUTH("1001", "非法访问");
    private String code;
    private String message;

    ResultCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
