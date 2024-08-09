package com.example.g5backend.serve.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.g5backend.serve.entity.po.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-10 14:14
 **/
@Mapper
public interface UserDao extends BaseMapper<User> {

    /**
     * 根据用户id返回对应用户角色列表
     * @param userId
     * @return
     */
    String[] findRolesByUserId(Long userId);

    /**
     * 删除用户角色关系
     * @param userId
     * @return
     */
    @Delete("DELETE FROM t_user_role WHERE u_id = #{userId}")
    int deleteUserRole(Long userId);

    /**
     * 根据用户名模糊查询
     * @return
     */
    List<User> selectUsersByUsername();

    /**
     * 根据用户id返回角色名称和id列表
     * @param userId
     * @return
     */
    Long[] findRolesIdByUserId(Long userId);

    /**
     * 保存用户角色关系
     * @param userId
     * @param rolesId
     * @return
     */
    int saveUserRoles(Long userId, List<Long> rolesId);

}