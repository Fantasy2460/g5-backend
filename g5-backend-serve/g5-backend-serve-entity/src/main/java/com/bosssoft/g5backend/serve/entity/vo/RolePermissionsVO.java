package com.bosssoft.g5backend.serve.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @name: RolePermissionsVO
 * @description
 * @author: 曹鹏翔
 * @create: 2024-08-07 16:01
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Long id;

    /**
     * 权限id列表
     */
    private Long[] permissionsId;

}
