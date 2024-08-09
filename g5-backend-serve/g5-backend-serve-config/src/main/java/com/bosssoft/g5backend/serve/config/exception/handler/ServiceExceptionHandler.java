package com.bosssoft.g5backend.serve.config.exception.handler;


import com.bosssoft.g5backend.serve.config.exception.ServiceException;
import com.example.g5backend.serve.utils.Result;
import com.example.g5backend.serve.utils.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-16 18:21
 **/

/**/
@ControllerAdvice
public class ServiceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public Result<Object> serviceExceptionHandler(ServiceException serviceException) {
        return Result.error().code(ResultCode.ERROR).message(serviceException.getMessage());
    }

}
