package com.bosssoft.g5backend.serve.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @program: rbac
 * @description 自定义异常处理类——身份异常处理
 * @author: ZhaoJixin
 * @create: 2022-06-11 20:57
 **/
public class CustomerAuthenticationException extends AuthenticationException {
    public CustomerAuthenticationException(String message) {
        super(message);
    }
}
