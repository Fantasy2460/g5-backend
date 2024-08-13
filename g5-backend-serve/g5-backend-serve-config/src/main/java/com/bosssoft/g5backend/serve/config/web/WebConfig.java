package com.bosssoft.g5backend.serve.config.web;

import com.bosssoft.g5backend.serve.config.annotation.ApiIdempotentInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @name: WebConfig
 * @description
 * @author: 曹鹏翔
 * @create: 2024-08-08 10:31
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private ApiIdempotentInterceptor apiIdempotentInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiIdempotentInterceptor);
    }

}
