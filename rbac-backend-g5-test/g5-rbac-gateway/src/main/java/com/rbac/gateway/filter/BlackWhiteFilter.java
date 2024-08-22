package com.rbac.gateway.filter;

import com.rbac.gateway.ResponseUtil;
import com.rbac.gateway.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.rbac.gateway.ResponseUtil.response;

/**
 * 黑白名单检查过滤器
 * kp:  在nacos配置中心， 信息： 以 id + 账号组成
 *      黑名单： 在请求访问时，黑名单成员禁止访问任何资源， 每次请求进行检查
 *      白名单： 在开启降级模式后， 只有白名单成员允许通过， 每次请求都进行检查
 * todo: 网关的异常处理
 * @author Lzzh
 * @version 1.0
 */
@Slf4j
@Component
public class BlackWhiteFilter implements GlobalFilter, Ordered {



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入{}, 请求路径为{}",this, exchange.getRequest().getURI());
//        return chain.filter(exchange);

        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().toString();
//        if(uri.contains("user/info") || uri.contains("user/logout")) {
//            chain.filter(exchange);
//        }
        Long userId = Long.valueOf(exchange.getAttributes().get("userId").toString());
        // 黑名单的验证， 是黑名单直接返回
        if(Boolean.TRUE.equals(SystemService.isBlackId(userId))) {
            return response(exchange, "账号异常,请联系管理员");
        }


        // 判断是否系统降级, 判断是否为白名单成员，非白名单成员禁止登录
        if(Boolean.TRUE.equals(SystemService.isDegradation())) {
            if(Boolean.TRUE.equals(SystemService.isWhiteId(userId))) {
                return chain.filter(exchange);
            } else {
                return ResponseUtil.response(exchange, "非白名单成员，联系管理员");
            }
        } else {
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
