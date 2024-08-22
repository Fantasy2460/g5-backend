package com.rbac.common.exception.handler;


import com.rbac.common.exception.Message;
import com.rbac.common.exception.ServiceException;
import com.rbac.common.util.Result;
import com.rbac.common.util.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: rbac
 * @description
 * @author: zzzhlee
 **/
@ControllerAdvice
public class ServiceExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public Result<Object> serviceExceptionHandler(ServiceException serviceException) {
        return Result.error().code(ResultCode.ERROR).message(serviceException.getMessage());
    }

}
