package com.bosssoft.g5backend.serve.config.exception;

/**
 * @name: ServiceException
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-08 17:01
 **/
public class ServiceException extends RuntimeException {
    public ServiceException(String msg) {
        super(msg);
    }
}
