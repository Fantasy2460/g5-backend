package com.bosssoft.g5backend.serve.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-11 20:42
 **/
@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 存缓存
     * @param key
     * @param value
     * @param timeOut
     */
    public void set(String key ,String value,Long timeOut){
        redisTemplate.opsForValue().set(key,value,timeOut, TimeUnit.SECONDS);
    }

    /**
     * 取缓存
     * @param key
     * @return
     */
    public String get(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 清除缓存
     * @param key
     */
    public void del(String key){
        redisTemplate.delete(key);
    }
}
