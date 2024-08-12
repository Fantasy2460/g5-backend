package com.example.g5backend.serve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @program: rbac
 * @description 接口拦截器
 * @author: 寒旅
 * @create: 2022-06-16 19:10
 **/

@MapperScan({"com.example.g5backend.serve.dao","com.bosssoft.g5backend.serve.config.redis"})
@SpringBootApplication
@Repository
@ServletComponentScan
@ComponentScan(basePackages = {"com.example.g5backend.serve.utils","com.bosssoft.g5backend.serve.config", "com.example.g5backend.serve.service"})
public class RbacApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbacApplication.class, args);
    }

}
