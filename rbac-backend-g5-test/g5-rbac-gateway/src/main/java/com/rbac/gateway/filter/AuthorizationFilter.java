package com.rbac.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.rbac.common.redis.RedisService;
import com.rbac.common.util.LoginResult;
import com.rbac.common.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 鉴权过滤器
 *      kp: 用户的每一次请求，都需要在此通过鉴权，验证用户是否有权限操作
 *      todo: 尝试结合redis
 * @author Lzzh
 * @version 1.0
 */
@Slf4j
@Component
public class AuthorizationFilter implements GlobalFilter, Ordered {
    /**
     * 用户登录
     */
    private final String LOGIN = "/sysUser/login";

    /**
     * 用户关于自身的操作
     */
    private final String SYS_USER = "/sysUser";

    @Resource
    RedisService redisService;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入{}",this);
        log.info("userId is  " + exchange.getAttributes().get("userId"));
        log.info("token is  " + exchange.getAttributes().get("token"));
        // 1、获取路径， 判断是否为登录， 正常返回，登录，直接返回
        String uri = exchange.getRequest().getURI().toString();

        if(uri.contains(LOGIN)) {
            // 封装信息
            String userId = String.valueOf(exchange.getAttributes().get("userId"));
            String token = (String) exchange.getAttributes().get("token");
            log.info("userId is  " + exchange.getAttributes().get("userId"));
            log.info("token is  " + exchange.getAttributes().get("token"));
            // 将 LoginResult 转换为 JSON 字符串
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("userId", userId)
                    .header("token", token)
                    .build();
            return chain.filter(exchange.mutate().request(request).build());
        }
        // 3、非登陆， 验证jwt信息是否正常，不正常直接返回
        if(uri.contains(SYS_USER)) {
            return chain.filter(exchange);
        }
        // 4、正常获取请求路径，从redis中获取用户的权限，不存在远程调用， 存入redis 验证
//        String operationType = getOperationType(uri);
//        String userId = (String) exchange.getAttributes().get("userId");
//        if (Boolean.FALSE.equals(redisService.isElementOfSet("user:" + userId, operationType))) {
//            // 5、无权限直接返回
//            return GatewayResponse.response(exchange, Result.error().message("无权限操作"));
//        }
        log.info("开始执行后端方法");
        return chain.filter(exchange);
    }

//    private String getOperationType(String uri) {
//        String[] split = uri.split("/");
//        return split[1];
//    }

    @Override
    public int getOrder() {
        return 2;
    }
}
