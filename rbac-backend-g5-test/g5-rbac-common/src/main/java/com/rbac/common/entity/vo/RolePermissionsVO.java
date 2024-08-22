package com.rbac.common.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色权限对应类
 * @program: rbac
 * @description
 * @author: zzzhlee
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
