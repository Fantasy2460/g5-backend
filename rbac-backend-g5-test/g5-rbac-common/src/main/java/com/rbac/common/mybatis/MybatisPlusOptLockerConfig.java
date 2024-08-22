package com.rbac.common.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: rbac
 * @description mybatis乐观锁配置类
 * @author: zzzhlee
 * @create: 2022-06-15 15:16
 **/
@EnableTransactionManagement
@Configuration
public class MybatisPlusOptLockerConfig {
    @Bean
    public MybatisPlusInterceptor optimisticLockerInterceptor() {
        return new MybatisPlusInterceptor();
    }
}
