package com.bosssoft.g5backend.serve.service.security.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bosssoft.g5backend.serve.config.redis.RedisService;
import com.bosssoft.g5backend.serve.service.UserService;
import com.bosssoft.g5backend.serve.utils.JwtUtils;
import com.bosssoft.g5backend.serve.utils.LoginResult;
import com.bosssoft.g5backend.serve.utils.ResultCode;
import com.bosssoft.g5backend.serve.entity.po.User;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @name: LoginSuccessHandler
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 15:11
 **/
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String TOKEN_PREFIX = "token_";

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisService redisService;

    @Resource
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        User user = (User) authentication.getPrincipal();
        user.setRoles(userService.findRolesByUserId(user.getId()));
        String token = jwtUtils.generateToken(user);
        long expireTime = Jwts.parser()
                .setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(token.replace("jwt_", ""))
                .getBody().getExpiration().getTime();
        LoginResult loginResult = new LoginResult(user.getId(),
                user.getPassword(),
                user.getIntroduction(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatar(),
                user.getRoles(),
                ResultCode.SUCCESS,
                token,
                expireTime);
        String result = JSON.toJSONString(loginResult, SerializerFeature.DisableCircularReferenceDetect);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(result.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
        StringBuilder tokenKey = new StringBuilder(TOKEN_PREFIX);
        tokenKey.append(token);
        redisService.set(tokenKey.toString(),token, jwtUtils.getExpiration() / 1000);
    }

}
