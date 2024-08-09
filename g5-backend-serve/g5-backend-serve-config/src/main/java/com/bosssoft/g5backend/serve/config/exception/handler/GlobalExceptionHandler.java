package com.bosssoft.g5backend.serve.config.exception.handler;


import com.example.g5backend.serve.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: rbac
 * @description 统一异常捕获
 * @author: 寒旅
 * @create: 2022-06-16 16:01
 **/

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> errorResult(Exception e){
        return Result.error().message("全局捕获异常错误！" + e.getMessage());
    }
}
