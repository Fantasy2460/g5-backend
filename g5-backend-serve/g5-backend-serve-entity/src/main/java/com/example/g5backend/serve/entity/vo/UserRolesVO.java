package com.example.g5backend.serve.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @name: UserRolesVO
 * @description
 * @author: 曹鹏翔
 * @create: 2024-08-07 11:33
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色id列表
     */
    private Long[] rolesId;

    /**
     * 角色名列表
     */
    private String[] roles;

}
