package com.bosssoft.g5backend.serve.config.exception.handler;


import com.bosssoft.g5backend.serve.config.exception.ServiceException;
import com.example.g5backend.serve.utils.Result;
import com.example.g5backend.serve.utils.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @name: ServiceExceptionHandler
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-08 15:21
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
