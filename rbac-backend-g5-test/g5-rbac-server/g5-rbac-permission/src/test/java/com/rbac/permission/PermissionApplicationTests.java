package com.rbac.permission;

import com.rbac.common.redis.RedisService;
import com.rbac.common.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Set;

@SpringBootTest
@ComponentScan(basePackages = {"com.rbac.common", "com.rbac.permission","com.rbac.feign"})
class PermissionApplicationTests {

    @Resource
    JwtUtils jwtUtils;
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @Resource
    RedisService redisService;


    @Test
    void contextLoads() {
        Set<String> keys = redisTemplate.keys("");
        System.out.println(keys);
        redisService.set("123","zxcvasd", 1000L);
    }


}
