package com.example.g5backend.serve.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-10 14:01
 **/
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     * 自增
     */
    private Long id;

    /**
     * 用户名（登录名称）
     */
    private String username;

    /**
     * 登录密码
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
     * 个人头像
     */
    private String avatar;

    /**
     * 角色列表
     */
    private String[] roles;

}
