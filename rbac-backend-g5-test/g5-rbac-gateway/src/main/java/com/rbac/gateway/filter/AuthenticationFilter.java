package com.rbac.gateway.filter;

import cn.hutool.json.JSONUtil;
import com.rbac.common.entity.vo.UserVO;
import com.rbac.common.util.LoginResult;
import com.rbac.common.util.Result;
import com.rbac.common.util.ResultCode;
import com.rbac.gateway.service.AuthenticationService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

import static com.rbac.gateway.ResponseUtil.response;

/**
 * token过滤器(第二层)
 *      在此过滤器中 生成/检查 token
 * @author Lzzh
 * @version 1.0
 */
@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    @Autowired
    AuthenticationService service;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入{}", this);
        ServerHttpRequest request = exchange.getRequest();

        log.info("登录路径" + request.getURI().toString());
        if(!request.getURI().toString().contains("/sysUser/loginOut") &&
                request.getURI().toString().contains("/sysUser/login")) {
            // 进入登录流程
            return DataBufferUtils.join(request.getBody()).flatMap(dataBuffer -> {
                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(bytes);
                String body = new String(bytes, StandardCharsets.UTF_8);
                // 恢复请求体的流，供后续的过滤器链使用
                DataBufferUtils.release(dataBuffer);

                // 解析请求参数
                MultiValueMap<String, String> map = new LinkedMultiValueMap();
                String[] pairs = body.split("&");
                for(String pair: pairs) {
                    String[] kv = pair.split("=");
                    if (kv.length > 1) {
                        map.add(kv[0], kv[1]);
                    }
                }

                // 获取到登录信息
                String username = map.getFirst("username");
                String password = map.getFirst("password");

                // JWT生成

                LoginResult info = null;
                try {
                    info = service.createJwt(username, password);
                    if(info == null) {
                        return response(exchange, "账号不存在或者密码错误");
                    }
                } catch (RuntimeException e) {
                    return response(exchange,"账号不存在或者密码错误");
                }
//                if(info == null) {
//                    return response(exchange, "账号不存在或者密码错误");
//                }
                info.setCode(ResultCode.SUCCESS);
                exchange.getAttributes().put("token", info.getToken());
                exchange.getAttributes().put("userId", info.getId());

                // 重新生成请求体数据
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                        .header("Token", info.getToken())
                        .build();
                return chain.filter(exchange.mutate().request(new ServerHttpRequestDecorator(mutatedRequest) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        // 返回新的请求体数据流
                        return Flux.just(exchange.getResponse().bufferFactory().wrap(bytes));
                    }
                }).build());
            });
        }
        // 解析token  判断是否过期
        String token = request.getHeaders().get("Token").get(0);
        UserVO userVO = null;
        try {
            Claims claims = service.parseToken(token);
            userVO = JSONUtil.toBean(claims.getSubject(), UserVO.class);
        } catch (RuntimeException e) {
            return response(exchange, "token已过期，请退出重新登录");
        }
        exchange.getAttributes().put("userId", userVO.getId());
        exchange.getAttributes().put("user", userVO);
        exchange.getRequest().mutate()
                .header("userId", userVO.getId().toString())
                .header("username", userVO.getUsername());
        return chain.filter(exchange);
    }



    @Override
    public int getOrder() {
        return 0;
    }
}
