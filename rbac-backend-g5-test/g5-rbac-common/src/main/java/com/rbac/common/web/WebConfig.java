package com.rbac.common.web;

import com.rbac.common.annotation.ApiIdempotentInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: rbac
 * @description 添加接口幂等性拦截器
 * @author: zzzhlee
 * @create: 2022-06-16 19:16
 **/
//@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Autowired
//    private ApiIdempotentInterceptor apiIdempotentInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(apiIdempotentInterceptor);
    }

}
