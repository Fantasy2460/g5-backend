package com.bosssoft.g5backend.serve.config.token;

import com.example.g5backend.serve.utils.Result;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * @program: rbac
 * @description 用户实现接口幂等性的Token服务接口
 * @author: 寒旅
 * @create: 2022-06-16 18:24
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
