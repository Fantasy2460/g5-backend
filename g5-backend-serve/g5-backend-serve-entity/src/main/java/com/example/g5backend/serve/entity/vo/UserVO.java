package com.example.g5backend.serve.entity.vo;


import com.example.g5backend.serve.entity.po.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @name: UserVO
 * @description
 * @author: 李自豪
 * @create: 2024-08-07 11:17
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 11, min = 3, message = "用户名长度在3到11个字符")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 个人介绍
     */
    @Size(max = 100, message = "个人介绍长度不要超过100哦")
    private String introduction;

    /**
     * 联系方式
     */
    @NotBlank(message = "联系方式不能为空")
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "电话号码格式错误")
    private String phone;

    /**
     * 邮箱地址
     */
    @NotBlank(message = "邮箱地址不能为空")
    @Email(message = "邮箱地址格式错误")
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色列表
     */
    private String[] roles;

    /**
     * 从PO用户数据中拷贝进VO数据
     * @param user
     */
    public void clone(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.introduction = user.getIntroduction();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.roles = user.getRoles();
    }
}
