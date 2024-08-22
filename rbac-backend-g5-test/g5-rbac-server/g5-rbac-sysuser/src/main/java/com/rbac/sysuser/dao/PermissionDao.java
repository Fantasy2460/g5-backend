package com.rbac.sysuser.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbac.common.entity.po.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @name: PermissionDao
 * @description 子系统模块Dao层
 * @author: 赵佶鑫
 * @create: 2024-08-07 11:11
 **/
@Mapper
public interface PermissionDao extends BaseMapper<Permission> {


    /**
     * 根据用户id获取权限列表
     * @param userId
     * @return List<Permission>
     */
    List<Permission> findPermissionListByUserId(Long userId);

    /**
     * 根据权限id生成对应角色信息
     * @param permissionId
     * @return String[]
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
     * @return boolean
     */
    boolean savePermissionRoles(Long id, Long[] roleIds);

    /**
     * 根据角色名返回角色Id
     * @param role
     * @return Long
     */
    @Select("SELECT id FROM t_role WHERE role_name = #{role}")
    Long selectRoleIdByRoleName(String role);

    /**
     * 获取所有的菜单列表
     * @param
     * @return List<Permission>
     */
    @Select("SELECT * FROM t_permission")
    List<Permission> selectList();

}