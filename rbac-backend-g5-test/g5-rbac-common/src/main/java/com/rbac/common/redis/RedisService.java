package com.rbac.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *  rbac redis服务
 * @description
 * @author: zzzhlee
 * @create: 2022-06-11 20:42
 **/
@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 存缓存
     * @param key key
     * @param value value
     * @param timeOut 时间
     */
    public void set(String key ,String value,Long timeOut){
        redisTemplate.opsForValue().set(key,value,timeOut, TimeUnit.SECONDS);
    }

    /**
     * 取缓存
     * @param key key
     * @return value
     */
    public String get(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 清除缓存
     * @param key key
     */
    public void del(String key){
        redisTemplate.delete(key);
    }

    /**
     * 查看键值是否存在
     * @param s key
     * @return 存在？
     */
    public Boolean hasKey(String s) {
        return redisTemplate.hasKey(s);
    }

    /**
     * 删除键值
     * @param s key
     * @return 删除
     */
    public Boolean delete(String s) {
        return redisTemplate.delete(s);
    }

    /**
     * 查看set是否存在value
     * @param key key
     * @param value value
     * @return 是否存在
     */
    public Boolean isElementOfSet(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 向set中添加元素
     * @param key key
     * @param values value
     * @param expireTime  过期时间
     * @return
     */
    public Long addElementToSet(String key, Set<String> values, Long expireTime) {
        Long amount = redisTemplate.opsForSet().add(key, values);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        return amount;
    }
}
