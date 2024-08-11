package com.bosssoft.g5backend.serve.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @program: rbac
 * @description mybatis乐观锁配置类
 * @author: 寒旅
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
