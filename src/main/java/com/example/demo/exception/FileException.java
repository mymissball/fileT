package com.example.demo.exception;

import com.example.demo.entity.ResultJson;
import org.springframework.web.bind.annotation.ExceptionHandler;


public class FileException extends SuperException {

    @ExceptionHandler(SuperException.class)
    public ResultJson getException(Throwable e){
        return new ResultJson(400,e.getMessage());
    }
}
