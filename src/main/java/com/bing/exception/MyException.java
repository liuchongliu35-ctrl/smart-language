package com.bing.exception;


import com.bing.bean.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class MyException<T> {
    @Autowired
    private Result<T> result;

    @ExceptionHandler(NullPointerException.class)
    public Result<T> nullPointerException(NullPointerException e){
        String message = e.getMessage();
        result.setCode("101");
        result.setMessage(message);
        return result;

    }

    @ExceptionHandler(SoundException.class)
    public Result<T> unableToMatchException(SoundException e){
        String message=e.getMessage();
        System.out.println(message);
        result.setCode("400");
        result.setData(null);
        result.setMessage(message);
        return result;
    }

    @ExceptionHandler(OutRangeException.class)
    public Result<T> outRangeException(OutRangeException e){
        String message=e.getMessage();
        System.out.println(message);
        result.setCode("501");
        result.setData(null);
        result.setMessage(message);
        return result;
    }

    @ExceptionHandler(ModelException.class)
    public Result<T> ModelException(ModelException e){
        String message=e.getMessage();
        System.out.println(message);
        result.setCode("502");
        result.setData(null);
        result.setMessage(message);
        return result;
    }

    @ExceptionHandler(DateBaseException.class)
    public Result<T> DataBaseException(DateBaseException e){
        String message=e.getMessage();
        System.out.println(message);
        result.setCode("503");
        result.setData(null);
        result.setMessage(message);
        return result;
    }
}
