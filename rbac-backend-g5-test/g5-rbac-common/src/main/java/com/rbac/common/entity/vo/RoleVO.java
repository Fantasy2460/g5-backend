package com.rbac.common.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 角色VO类
 * @program: rbac
 * @description 权限的集合
 * @author: zzzhlee
 **/
@Data
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编号
     * 自增
     */
    private Long id;

    /**
     * 键值
     */
    private String key;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名不能为空")
    private String name;

    /**
     * 角色描述
     */
    @NotBlank(message = "角色描述不能为空")
    private String description;

    /**
     * 角色对应角色数组
     */
    private List<RouterVO> routes;

}
