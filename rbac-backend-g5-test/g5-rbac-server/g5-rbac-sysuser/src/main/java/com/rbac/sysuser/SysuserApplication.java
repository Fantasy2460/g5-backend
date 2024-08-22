package com.rbac.sysuser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 * @program: SysuserApplication
 * @description 用户子系统启动类
 * @author: 赵佶鑫
 * @create: 2024-08-23 19:32
 **/
@SpringBootApplication
@MapperScan("com.rbac.sysuser.dao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.rbac.feign"})
public class SysuserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysuserApplication.class, args);
    }

}
