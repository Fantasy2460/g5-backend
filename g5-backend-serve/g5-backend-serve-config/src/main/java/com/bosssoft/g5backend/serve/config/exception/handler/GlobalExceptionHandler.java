package com.bosssoft.g5backend.serve.config.exception.handler;


import com.bosssoft.g5backend.serve.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @name: LoginResult
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 17:01
 **/

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> errorResult(Exception e){
        return Result.error().message("全局捕获异常错误！" + e.getMessage());
    }
}
