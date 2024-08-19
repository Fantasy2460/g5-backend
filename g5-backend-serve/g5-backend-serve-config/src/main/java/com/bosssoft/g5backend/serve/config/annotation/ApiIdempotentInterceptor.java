package com.bosssoft.g5backend.serve.config.annotation;

import com.alibaba.druid.util.StringUtils;
import com.bosssoft.g5backend.serve.config.token.TokenService;
import com.bosssoft.g5backend.serve.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * @name: LoginResult
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 17:01
 **/
@Slf4j
@Component
public class ApiIdempotentInterceptor implements HandlerInterceptor {

    private static final String API_IDEMPOTENT = "ai";
    private static final String API_IDEMPOTENT_PREFIX = "ai_";

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入拦截器");
        if ((handler instanceof HandlerMethod)) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            ApiIdempotent methodAnnotation = method.getAnnotation(ApiIdempotent.class);
            if (methodAnnotation != null){
                // 校验通过放行，校验不通过全局异常捕获后输出返回结果
                HttpSession session = request.getSession();
                Result token = tokenService.createToken();
                if (token != null) {
                    session.setAttribute(API_IDEMPOTENT, API_IDEMPOTENT_PREFIX + token.getMessage());
                    log.info("创建ai成功" + API_IDEMPOTENT_PREFIX + token.getMessage());
                    return true;
                }
                tokenService.checkToken(request);
                if (isRepeatSubmit(request)) {
                    log.warn("禁止重复提交" + request.getRequestURI());
                    return false;
                }
                session.removeAttribute(API_IDEMPOTENT);
            }
        } else {
            return HandlerInterceptor.super.preHandle(request, response,handler);
        }
        return true;
    }

    private boolean isRepeatSubmit(HttpServletRequest request) {
        String token = (String) request.getSession().getAttribute(API_IDEMPOTENT);
        if (StringUtils.isEmpty(token)) {
            return true;
        }
        String reqToken = request.getHeader(API_IDEMPOTENT);
        if (StringUtils.isEmpty(reqToken)) {
            reqToken = request.getParameter(API_IDEMPOTENT);
            if (StringUtils.isEmpty(reqToken)) {
                return true;
            }
        }
        return !token.equals(reqToken);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 无需实现
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 无需实现
    }
}
