package com.example.g5backend.serve.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: rbac
 * @description 权限类
 * @author: 寒旅
 * @create: 2022-06-12 09:58
 **/
@Data
public class PermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限编号
     */
    private Long id;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 路由组件
     */
    private String component;

    /**
     * 权限名称
     */
    private String title;

    /**
     * 权限图标
     */
    private String icon;

    /**
     * 父组件id
     */
    private Long pid;

    /**
     * 授权标识符
     */
    private String code;

}