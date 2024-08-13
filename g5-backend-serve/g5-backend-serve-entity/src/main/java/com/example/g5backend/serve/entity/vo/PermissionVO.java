package com.example.g5backend.serve.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @name: PermissionVO
 * @description
 * @author: 罗睿信
 * @create: 2024-08-07 14:15
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限编号
     */
    private Long id;

    /**
     * 父组件id
     */
    private Long pid;

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
     * 角色列表
     */
    private String[] roles;

}
