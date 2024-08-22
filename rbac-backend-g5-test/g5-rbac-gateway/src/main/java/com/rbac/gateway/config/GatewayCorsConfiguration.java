package com.rbac.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 配置网关的跨域请求
 * @author Lzzh
 * @version 1.0
 */
@Configuration
public class GatewayCorsConfiguration {
    /**
     * 配置网关的跨域请求， 作用在整体顶层，进行粗略的过滤处理
     */
    @Bean
    public CorsWebFilter corsWebFilter(){
        // 跨域配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 跨域配置
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 1 配置跨域
        // 允许所有头进行跨域
        corsConfiguration.addAllowedHeader("*");
        // 允许所有请求方式进行跨域
        corsConfiguration.addAllowedMethod("*");
        // 允许所有请求来源进行跨域
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedOrigin("http://localhost:9527/*");
        corsConfiguration.addAllowedOriginPattern("*");
//        corsConfiguration.addAllowedOriginPattern("*");
        // 允许携带cookie进行跨域
        corsConfiguration.setAllowCredentials(true);
        // 2 任意路径都允许第1步配置的跨域
        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(source);
    }
}
