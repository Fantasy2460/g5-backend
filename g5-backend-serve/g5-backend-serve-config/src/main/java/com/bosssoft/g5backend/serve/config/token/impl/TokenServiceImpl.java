package com.bosssoft.g5backend.serve.config.token.impl;

import com.bosssoft.g5backend.serve.config.exception.ServiceException;
import com.bosssoft.g5backend.serve.config.redis.RedisService;
import com.bosssoft.g5backend.serve.config.token.TokenService;
import com.example.g5backend.serve.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @name: TokenServiceImpl
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 10:21
 **/
@Service
public class TokenServiceImpl implements TokenService {

    private static final String API_IDEMPOTENT = "ai";
    private static final String API_IDEMPOTENT_PREFIX = "ai_";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RedisService redisService;

    /**
     * 创建Token
     * @return
     */
    @Override
    public Result createToken() {
        String ai = UUID.randomUUID().toString().replace("-","");
        redisService.set(API_IDEMPOTENT_PREFIX + ai, ai, 3000L);
        return Result.ok().message(ai);
    }

    /**
     * 校验是否存在指定token 与token过滤器中不同
     * @param request
     * @return
     */
    @Override
    public Result checkToken(HttpServletRequest request) {
        String ai = request.getHeader(API_IDEMPOTENT);
        if (ObjectUtils.isEmpty(ai)) {
            ai = request.getParameter(API_IDEMPOTENT);
        }
        if (ObjectUtils.isEmpty(ai)) {
            throw new ServiceException("apiIdempotent不存在");
        }
        if (Boolean.FALSE.equals(redisTemplate.hasKey(API_IDEMPOTENT_PREFIX + ai))) {
            throw new ServiceException("请求重复,ai已经被删除");
        }
        Boolean delete = redisTemplate.delete(API_IDEMPOTENT_PREFIX + ai);
        if (Boolean.FALSE.equals(delete)) {
            throw new ServiceException("请求重复，ai删除失败");
        }
        return Result.ok().message("apiIdempotent校验成功");
    }
}
