package com.rbac.gateway;

import cn.hutool.json.JSONUtil;
import com.rbac.common.util.Result;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Lzzh
 * @version 1.0
 */
public class ResponseUtil {
    public static Mono<Void> response(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();

        Result<Object> res = Result.error().code(500).message(message);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(res).getBytes());

        return response.writeWith(Mono.just(dataBuffer));
    }
}
