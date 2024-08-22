package com.rbac.user.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbac.common.entity.po.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.util.List;

/**
 * @name: UserDao
 * @description: 用户dao层，管理数据库交互逻辑
 * @author: 赵佶鑫
 * @create: 2022-08-07 14:34
 **/
@Mapper
public interface UserDao extends BaseMapper<User> {

    /**
     * 根据用户id返回对应用户角色列表
     * @param userId
     * @return String[]
     */
    @Select("SELECT r.role_name" +
            "        FROM t_role AS r" +
            "            LEFT JOIN t_user_role AS ur ON r.id = ur.r_id" +
            "            LEFT JOIN t_user AS u ON u.id = ur.u_id" +
            "        WHERE u.id = #{userId}" +
            "        ORDER BY r.id ASC")
    String[] findRolesByUserId(@Param("userId") Long userId);

    /**
     * 删除用户角色关系
     * @param userId
     * @return int
     */
    @Delete("DELETE FROM t_user_role WHERE u_id = #{userId}")
    int deleteUserRole(Long userId);

    /**
     * 根据用户名模糊查询
     * @param username
     * @return User
     */
    User selectUsersByUsername(String username);

    /**
     * 根据用户id返回角色名称和id列表
     * @param userId
     * @return Long[]
     */
    @Select("        SELECT r.id" +
            "        FROM t_role AS r" +
            "                 LEFT JOIN t_user_role AS ur ON r.id = ur.r_id" +
            "                 LEFT JOIN t_user AS u ON u.id = ur.u_id" +
            "        WHERE u.id = #{userId}\n" +
            "        ORDER BY r.id ASC")
    Long[] findRolesIdByUserId(@Param("userId") Long userId);

    /**
     * 保存用户角色关系
     * @param userId
     * @param rolesId
     * @return int
     */
    int saveUserRoles(Long userId, List<Long> rolesId);

}
