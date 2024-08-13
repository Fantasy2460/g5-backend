package com.bosssoft.g5backend.serve.config.token;

import com.example.g5backend.serve.utils.Result;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * @name: TokenService
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-08 11:01
 **/
@Service
public interface TokenService {

    /**
     * 创建Token
     * @return
     */
    Result createToken();

    /**
     * 校验是否存在指定token 与token过滤器中不同
     * @param request
     * @return
     */
    Result checkToken(HttpServletRequest request);

}
