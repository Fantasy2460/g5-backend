package com.example.g5backend.serve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @program: rbac
 * @description 接口拦截器
 * @author: 寒旅
 * @create: 2022-06-16 19:10
 **/

@MapperScan("com.example.g5backend.serve.dao")
@SpringBootApplication
@ServletComponentScan
public class RbacApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbacApplication.class, args);
    }

}
