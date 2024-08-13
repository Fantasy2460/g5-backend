package com.bosssoft.g5backend.serve.config.mybatis;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * className: MyBatisConfig
 * @author 曹鹏翔
 * @description:
 * @date 2024/8/8 14:50
 */
@Configuration
@MapperScan(basePackages = "com.example.g5backend.serve.dao", sqlSessionFactoryRef = "sqlSessionFactory")
public class MyBatisConfig {
    @Resource
    private DataSource dataSource;

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return sessionFactory;
    }
}
