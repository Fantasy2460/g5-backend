package com.rbac.common.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: rbac
 * @description 用户所有角色
 * @author: zzzhlee
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
