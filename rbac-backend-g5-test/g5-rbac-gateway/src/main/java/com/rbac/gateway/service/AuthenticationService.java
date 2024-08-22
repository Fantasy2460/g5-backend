package com.rbac.gateway.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.rbac.common.entity.vo.UserVO;
import com.rbac.common.util.LoginResult;
import com.rbac.feign.user.UserClient;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 提供认证服务
 *      生成/检查 jwt: header:payload:signature
 * @author Lzzh
 * @version 1.0
 */
@Slf4j
@Service
public class AuthenticationService {


    private static  final String SALT = "asdf";

    /**
     * JWT 的过期时间
     */
    private static final Long EXPIRE_TIME = 18L;

    /**
     * 加密密码
     */
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    /**
     * 用户客户端
     */
    @Autowired
    UserClient userClient;




    private final Map<String, String> map = new HashMap<>();

    @PostConstruct
    private void initHeader() {
        map.put("alg", "HS256");
        map.put("typ", "JWT");
    }

    public Boolean validatePassword(String password, String passwordFromDB) {
        if (PASSWORD_ENCODER.matches(password, passwordFromDB)) {
            return true;
        }
        return false;
    }


    public LoginResult createJwt(String username, String password) {

        UserVO userVO =  userClient.queryUserByUsername(username).getData();
        if(userVO == null) {
            return null;
        }
        LoginResult loginResult = BeanUtil.copyProperties(userVO, LoginResult.class);
        // 检验密码
        if (!validatePassword(password, userVO.getPassword())) {
            return null;
        }
        // 通过userVO生成payload, 设置 JWT 的声明部分
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("id", userVO.getId());
        claims.put("loginTime", System.currentTimeMillis());
        Date expireTime = new Date(System.currentTimeMillis() + EXPIRE_TIME);
//        if(SystemService.isWhiteId(userVO.getId())) {
            expireTime = new Date( System.currentTimeMillis() + 18000000000L);
//        }
        String jwt = Jwts.builder()
                .setId(userVO.getId().toString()) //唯一的ID
                .setSubject(JSONUtil.toJsonStr(claims)) // 主题 可以是JSON数据
                .setIssuer("yu") // 签发者
                .setIssuedAt(new Date()) // 签发时间
                .setExpiration(expireTime) // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, SALT) //使用HS256对称加密算法签 名, 第二个参数为秘钥
                .compact();
        loginResult.setToken(jwt);
        return loginResult;

    }


    /**
     *     解析 JWT
     */
    public  Claims parseToken(String token) {
        // 解析并返回声明
        return Jwts.parser()
                .setSigningKey(SALT) // 设置用于解析的密钥
                .parseClaimsJws(token) // 解析 JWT
                .getBody(); // 获取声明部分
    }

    // 从 JWT 中获取用户名
    public  String getUserIdFromToken(String token) {
        // 从声明中提取用户名
        return parseToken(token).get("id", String.class);
    }

    // 验证 JWT 是否过期
    public  boolean isTokenExpired(String token) {
        // 获取声明中的过期时间
        Date expiration = parseToken(token).getExpiration();
        // 检查当前时间是否在过期时间之后
        return expiration.before(new Date());
    }

    // 验证 JWT 是否有效
    public  boolean validateJwt(String jwt) {
        try {
            // 检查id是否匹配且 JWT 是否未过期
            return (!isTokenExpired(jwt));
        } catch (Exception e) {
            // 如果解析过程中抛出任何异常，表明 JWT 无效
            return false;
        }
    }
}
