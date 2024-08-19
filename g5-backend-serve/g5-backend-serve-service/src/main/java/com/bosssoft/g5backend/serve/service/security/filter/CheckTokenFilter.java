package com.bosssoft.g5backend.serve.service.security.filter;

import com.bosssoft.g5backend.serve.config.exception.CustomerAuthenticationException;
import com.bosssoft.g5backend.serve.config.redis.RedisService;
import com.bosssoft.g5backend.serve.service.security.CustomerUserDetailsServiceImpl;
import com.bosssoft.g5backend.serve.service.security.handler.LoginFailureHandler;
import com.bosssoft.g5backend.serve.utils.JwtUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @name: CheckTokenFilter
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 14:24
 **/
@Slf4j
@Data
@Component
public class CheckTokenFilter extends OncePerRequestFilter {

    /**
     * 登录请求地址
     */
    @Value("${request.login.url}")
    private String loginUrl;

    @Resource
    private RedisService redisService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private CustomerUserDetailsServiceImpl customerUserDetailsService;

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String url = request.getRequestURI();
            if (!url.equals(loginUrl)) {
                this.validateToken(request);
            }
        } catch (AuthenticationException e) {
            loginFailureHandler.onAuthenticationFailure(request, response, e);
        }
        doFilter(request, response, filterChain);
    }

//    @Override
//    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//    }

    /**
     * 校验token
     * @param request
     */
    private void validateToken(HttpServletRequest request) {
        log.info("开始校验token信息");
        String token = request.getHeader("token");
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        if (ObjectUtils.isEmpty(token)) {
            throw new CustomerAuthenticationException("token不存在");
        }
        String tokenKey = "token_" + token;
        String redisToken = redisService.get(tokenKey);
        if (ObjectUtils.isEmpty(redisToken)) {
            throw new CustomerAuthenticationException("token已过期");
        }
        if (!token.equals(redisToken)) {
            throw new CustomerAuthenticationException("token验证失败");
        }
        String username = jwtUtils.getUsernameFromToken(token);
        if (ObjectUtils.isEmpty(username)) {
            throw new CustomerAuthenticationException("token解析失败");
        }
        UserDetails userDetails = customerUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new CustomerAuthenticationException("token验证失败");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("结束校验token信息");
    }


}
