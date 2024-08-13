package com.example.g5backend.serve.entity.dto;


import com.example.g5backend.serve.entity.po.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @name: RoleDTO
 * @description
 * @author: 曹鹏翔
 * @create: 2024-08-07 10:03
 **/
@Data
public class RoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编号
     */
    private Long id;

    /**
     * 键值
     */
    private String key;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色权限
     */
    private List<Permission> routes;

}
