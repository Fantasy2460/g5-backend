package com.example.g5backend.serve.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: rbac
 * @description 封装token返回的信息
 * @author: 寒旅
 * @create: 2022-06-10 21:31
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
