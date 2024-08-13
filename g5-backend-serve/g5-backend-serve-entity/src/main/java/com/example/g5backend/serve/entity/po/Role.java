package com.example.g5backend.serve.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @name: Role
 * @description
 * @author: 曹鹏翔
 * @create: 2024-08-07 10:32
 **/
@Data
@TableName("t_role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编号
     * 自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 键值
     */
    @TableField(value = "role_key")
    private String roleKey;

    /**
     * 角色名称
     */
    @TableField(value = "role_name")
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 账户是否可用(0 可用， 1删除用户)
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    /**
     * 角色对应角色数组
     */
    @TableField(exist = false)
    private List<Permission> routes;

}
