package com.rbac.permission.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbac.common.entity.po.Permission;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * @program: rbac
 * @description
 * @author: zzzhlee
 * @create: 2022-06-12 10:11
 **/
@org.apache.ibatis.annotations.Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据用户id获取权限列表
     * @param userId 用户id
     * @return
     */
    List<Permission> findPermissionListByUserId(Long userId);

    /**
     * 根据权限id生成对应角色信息
     * @param permissionId
     * @return
     */
    @Select("  SELECT DISTINCT r.role_name" +
            "            FROM t_permission AS p" +
            "                 LEFT JOIN t_role_permission AS rp ON p.id = rp.p_id" +
            "                 LEFT JOIN t_role AS r ON r.id = rp.r_id" +
            "            WHERE p.id = #{permissionId} AND p.is_deleted = 0")
    String[] findRolesByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 删除角色拥有的权限
     * @param id  角色id
     */
    @Delete("DELETE FROM t_role_permission WHERE p_id = #{id}")
    int deletePermissionRole(@Param("id") Long id);

    /**
     * 保存角色最新的权限
     * @param id 权限id
     * @param roleId 角色id
     * @return
     */
    @Insert("insert into t_role_permission(r_id,p_id) values(#{roleId}, #{id})")
    int savePermissionRoles(@Param("id") Long id, @Param("roleId") Long roleId);

    /**
     * 根据角色ID查询到所有权限
     * @param roleIds 角色id
     * @return 权限
     */
    @Select({
            "<script>",
            "SELECT p.* FROM t_permission p",
            "INNER JOIN t_role_permission rp ON p.id = rp.p_id",
            "WHERE rp.r_id IN",
            "<foreach item='roleId' index='index' collection='roleIds' open='(' separator=',' close=')'>",
            "#{roleId}",
            "</foreach>",
            "</script>"
    })
    List<Permission> findPermissionListByRoleIds(@Param("roleIds") Set<Long> roleIds);

//    /**
//     * 根据角色名返回角色Id
//     * @param role
//     * @return
//     */
//    @Select("SELECT id FROM t_role WHERE role_name = #{role}")
//    Long selectRoleIdByRoleName(String role);
}
