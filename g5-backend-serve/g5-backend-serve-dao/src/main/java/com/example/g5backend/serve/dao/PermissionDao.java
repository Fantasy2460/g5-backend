package com.example.g5backend.serve.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.g5backend.serve.entity.po.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-12 10:11
 **/
@Mapper
public interface PermissionDao extends BaseMapper<Permission> {


    /**
     * 根据用户id获取权限列表
     * @param userId
     * @return
     */
    List<Permission> findPermissionListByUserId(Long userId);

    /**
     * 根据权限id生成对应角色信息
     * @param permissionId
     * @return
     */
    String[] findRolesByPermissionId(Long permissionId);

    /**
     * 删除角色权限
     * @param id
     */
    @Delete("DELETE FROM t_role_permission WHERE p_id = #{id}")
    void deletePermissionRole(Long id);

    /**
     * 保存角色权限
     * @param id
     * @param roleIds
     * @return
     */
    boolean savePermissionRoles(Long id, Long[] roleIds);

    /**
     * 根据角色名返回角色Id
     * @param role
     * @return
     */
    @Select("SELECT id FROM t_role WHERE role_name = #{role}")
    Long selectRoleIdByRoleName(String role);


}
