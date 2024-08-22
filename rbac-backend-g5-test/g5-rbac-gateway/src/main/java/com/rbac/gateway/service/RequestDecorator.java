package com.rbac.gateway.service;

import cn.hutool.json.JSONUtil;
import com.rbac.common.util.LoginResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 网关返回数据
 *      封装在网关的返回数据的类型
 * @author Lzzh
 * @version 1.0
 */
public class RequestDecorator {
    /**
     * 封装请求信息
     * @param exchange 交换机
     * @return 封装返回前端数据
     */
    public static  Mono<Void> requestDecorate(ServerWebExchange exchange, WebFilterChain chain, String name) {
        // 获取在之前过滤器中存储的 LoginResult
        LoginResult result = (LoginResult) exchange.getAttributes().get("info");
        if (result != null) {
            // 将 LoginResult 转换为 JSON 字符串
            String resultJson = JSONUtil.toJsonStr(result);

            // 创建新的请求体 DataBuffer
            byte[] bytes = resultJson.getBytes(StandardCharsets.UTF_8);
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(bytes);

            // 使用 ServerHttpRequestDecorator 来包装原始请求，替换请求体
            ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    // 返回新的请求体
                    return Flux.just(dataBuffer);
                }

                @Override
                public HttpHeaders getHeaders() {
                    HttpHeaders headers = new HttpHeaders();
                    headers.putAll(super.getHeaders());
                    // 更新内容长度
                    headers.setContentLength(bytes.length);
                    return headers;
                }
            };

            // 继续过滤器链，使用新的请求对象
            return chain.filter(exchange.mutate().request(requestDecorator).build());
        }

        // 如果没有需要封装的内容，继续正常的过滤器链
        return chain.filter(exchange);
    }


}
