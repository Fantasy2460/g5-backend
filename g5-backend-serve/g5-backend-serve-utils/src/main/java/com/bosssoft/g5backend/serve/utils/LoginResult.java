package com.bosssoft.g5backend.serve.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name: LoginResult
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 17:01
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResult {
    /**
     * 用户编号
     */
    private Long id;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 自我介绍
     */
    private String introduction;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色列表
     */
    private String[] roles;

    /**
     * 状态码
     */
    private int code;

    /**
     * token令牌
     */
    private String token;

    /**
     * token过期时间
     */
    private Long expireTime;
}
