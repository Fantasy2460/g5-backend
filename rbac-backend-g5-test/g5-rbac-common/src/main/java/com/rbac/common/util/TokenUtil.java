package com.rbac.common.util;

/**
 * @author Lzzh
 * @version 1.0
 */

import com.rbac.common.exception.ServiceException;
import com.rbac.common.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @program: rbac
 * @description
 * @author: zzzhlee
 * @create: 2022-06-16 18:28
 **/
@Component
public class TokenUtil {

    private static final String API_IDEMPOTENT = "ai";
    private static final String API_IDEMPOTENT_PREFIX = "ai_";


    private static RedisService redisService;

    @Autowired
    public TokenUtil(RedisService redisService) {
        TokenUtil.redisService = redisService;
    }

    /**
     * 创建Token
     * @return
     */
    public static Result createToken() {
        String ai = UUID.randomUUID().toString().replace("-","");
        redisService.set(API_IDEMPOTENT_PREFIX + ai, ai, 3000L);
        return Result.ok().message(ai);
    }

    /**
     * 校验是否存在指定token 与token过滤器中不同
     * @param request
     * @return
     */
    public static Result checkToken(HttpServletRequest request) {
        String ai = request.getHeader(API_IDEMPOTENT);
        if (ObjectUtils.isEmpty(ai)) {
            ai = request.getParameter(API_IDEMPOTENT);
        }
        if (ObjectUtils.isEmpty(ai)) {
            throw new ServiceException("apiIdempotent不存在");
        }
        if (Boolean.FALSE.equals(redisService.hasKey(API_IDEMPOTENT_PREFIX + ai))) {
            throw new ServiceException("请求重复,ai已经被删除");
        }
        Boolean delete = redisService.delete(API_IDEMPOTENT_PREFIX + ai);
        if (Boolean.FALSE.equals(delete)) {
            throw new ServiceException("请求重复，ai删除失败");
        }
        return Result.ok().message("apiIdempotent校验成功");
    }
}
