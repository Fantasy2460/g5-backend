package com.bosssoft.g5backend.serve.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @name: MybatisPlusOptLockerConfig
 * @description
 * @author: 曹鹏翔
 * @create: 2024-08-08 11:12
 **/
@EnableTransactionManagement
@Configuration
public class MybatisPlusOptLockerConfig {
    @Bean
    public MybatisPlusInterceptor optimisticLockerInterceptor() {
        return new MybatisPlusInterceptor();
    }

}
