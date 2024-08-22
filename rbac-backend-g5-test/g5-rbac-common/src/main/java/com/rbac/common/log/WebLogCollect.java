package com.rbac.common.log;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @program: rbac
 * @description AOP controller层log
 * @author: zzzhlee
 **/

@Aspect
@Component
public class WebLogCollect {

    private static final Logger logger = LoggerFactory.getLogger(WebLogCollect.class);

    /**
     * 切面
     */
    @Pointcut("execution(public * com.bosssoft.rbac.controller.*.*(..))")
    public void webLog() {
        // 切面
    }

    /**
     * 前置通知
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
            String url = "URL : " + request.getRequestURL().toString();
            String httpMethod = "HTTP_METHOD : " + request.getMethod();
            String ip = "IP : " + request.getRemoteAddr();
            logger.info(url);
            logger.info(httpMethod);
            logger.info(ip);
            Enumeration<String> enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String name = enu.nextElement();
                logger.info("name:{},value:{}", name, request.getParameter(name));
            }
        } catch (Exception e) {
            logger.error("切面接收参数出现空指针异常！");
        }
    }

    /**
     * 后置通知
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        String response = new StringBuilder("RESPONSE : ").append(ret).toString();
        logger.info(response);
    }
}
