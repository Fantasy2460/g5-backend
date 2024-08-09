package com.example.g5backend.serve.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-22 14:26
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
