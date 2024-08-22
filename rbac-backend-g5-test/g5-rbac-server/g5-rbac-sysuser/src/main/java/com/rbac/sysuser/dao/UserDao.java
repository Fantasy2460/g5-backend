package com.rbac.sysuser.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbac.common.entity.po.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @name: UserDao
 * @description: 用户dao层
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
    String[] findRolesByUserId(Long userId);

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
    Long[] findRolesIdByUserId(Long userId);

    /**
     * 保存用户角色关系
     * @param userId
     * @param rolesId
     * @return int
     */
    int saveUserRoles(Long userId, List<Long> rolesId);


}