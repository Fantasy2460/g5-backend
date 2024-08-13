package com.bosssoft.g5backend.serve.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @name: CustomerAuthenticationException
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-08 14:29
 **/
public class CustomerAuthenticationException extends AuthenticationException {
    public CustomerAuthenticationException(String message) {
        super(message);
    }
}
