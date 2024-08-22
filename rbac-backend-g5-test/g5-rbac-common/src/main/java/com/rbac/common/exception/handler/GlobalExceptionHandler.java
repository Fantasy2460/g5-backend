package com.rbac.common.exception.handler;

import com.rbac.common.exception.Message;
import com.rbac.common.util.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: rbac
 * @description 统一异常捕获
 * @author: zzzhlee
 * @create: 2022-06-16 16:01
 **/

@ControllerAdvice
public class GlobalExceptionHandler {

    Message message;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<Object> errorResult(Exception e){
        return Result.error().message(message.Global() +e.getMessage());
    }

}
