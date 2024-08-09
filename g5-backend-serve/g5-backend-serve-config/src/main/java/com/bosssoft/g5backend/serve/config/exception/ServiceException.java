package com.bosssoft.g5backend.serve.config.exception;

/**
 * @program: rbac
 * @description 服务异常处理
 * @author: 寒旅
 * @create: 2022-06-16 18:19
 **/
public class ServiceException extends RuntimeException {
    public ServiceException(String msg) {
        super(msg);
    }
}
