package com.rbac.common.annotation;



import lombok.extern.slf4j.Slf4j;

// todo Token service引用的是其他包中的
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static com.rbac.common.util.TokenUtil.checkToken;

/**
 * @program: rbac
 * @description 接口拦截器
 * @author: zzzhlee
 * @create: 2022-06-16 19:10
 **/
@Slf4j
@Component
public class ApiIdempotentInterceptor implements HandlerInterceptor {

    private static final String API_IDEMPOTENT = "ai";
    private static final String API_IDEMPOTENT_PREFIX = "ai_";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod= (HandlerMethod) handler;
        Method method=handlerMethod.getMethod();
        ApiIdempotent methodAnnotation=method.getAnnotation(ApiIdempotent.class);
        if (methodAnnotation != null){
            // 校验通过放行，校验不通过全局异常捕获后输出返回结果
            checkToken(request);
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

}
