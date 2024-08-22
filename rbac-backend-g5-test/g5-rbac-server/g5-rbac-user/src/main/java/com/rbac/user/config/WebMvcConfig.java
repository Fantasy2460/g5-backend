package com.rbac.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @name: WebMvcConfig
 * @description 跨域配置
 * @author: 赵佶鑫
 * @create: 2024-08-10 11:11
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 用于配置前端跨域问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // .allowedOrigins("http://localhost:9527")
                .allowedMethods("*")
                .allowedOrigins("*")
                .allowedHeaders("*");
    }

}
