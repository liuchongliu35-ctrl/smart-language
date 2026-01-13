package com.bing.exception;

import com.bing.bean.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 返回 500 错误
    public ResponseEntity<Result> handleAllExceptions(Exception ex) {
        // 可以在这里记录错误日志
        System.out.println(ex.getMessage());
        // 返回统一的错误响应
        Result result=new Result<>();
        result.build(null,"111","客户端繁忙！");
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


