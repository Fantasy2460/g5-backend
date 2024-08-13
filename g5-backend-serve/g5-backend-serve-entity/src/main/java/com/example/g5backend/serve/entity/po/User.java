package com.example.g5backend.serve.entity.po;

import com.baomidou.mybatisplus.annotation.*;
import com.example.g5backend.serve.entity.vo.UserVO;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @name: User
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 10:27
 **/
@Data
@TableName("t_user")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户编号
     * 自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（登录名称）
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 自我介绍
     */
    private String introduction;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 邮箱地址
     */
    @NotBlank(message = "邮箱地址不能为空")
    @Email(message = "邮箱地址格式错误")
    private String email;

    /**
     * 个人头像
     */
    private String avatar = null;

    /**
     * 帐户是否过期(1 未过期，0已过期) 以下四个boolean变量是实现UserDetails接口必须的变量 且名称无法改变 故名字前缀为is
     */
    private boolean isAccountNonExpired = true;

    /**
     * 帐户是否被锁定(1 未过期，0已过期)
     */
    private boolean isAccountNonLocked = true;

    /**
     * 密码是否过期(1 未过期，0已过期)
     */
    private boolean isCredentialsNonExpired = true;

    /**
     * 帐户是否可用(1 可用，0 删除用户)
     */
    private boolean isEnabled = true;

    /**
     * 账户是否被删除（ 1-已经被删除但未清除数据 0-可用）
     */
    @TableLogic
    @TableField(value = "is_deleted")
    private Integer deleted;

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
     * 乐观锁
     */
    @Version
    private Integer version;

    /**
     * 角色列表
     */
    @TableField(exist = false)
    private String[] roles;

    /**
     * 权限列表
     */
    @TableField(exist = false)
    transient Collection<? extends GrantedAuthority> authorities;

    /**
     * 用户权限列表
     */
    @TableField(exist = false)
    private List<Permission> permissionList;

    public UserVO transformPoToVo() {
        UserVO user = new UserVO();
        user.setId(this.id);
        user.setPassword(this.password);
        user.setUsername(this.username);
        user.setIntroduction(this.introduction);
        user.setPhone(this.phone);
        user.setEmail(this.email);
        user.setAvatar(this.avatar);
        user.setRoles(this.roles);
        return user;
    }
}
