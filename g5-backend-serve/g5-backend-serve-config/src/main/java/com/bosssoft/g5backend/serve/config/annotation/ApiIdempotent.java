package com.bosssoft.g5backend.serve.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: rbac
 * @description 接口幂等性自定义注解
 * @author: 寒旅
 * @create: 2022-06-16 19:09
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiIdempotent {
}
