package com.rbac.common.exception;

/**
 * @program: rbac
 * @description 服务异常处理
 * @author: zzzhlee
 * @create: 2022-06-16 18:19
 **/
public class ServiceException extends RuntimeException {
    public ServiceException(String msg) {
        super(msg);
    }
}
