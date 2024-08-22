package com.rbac.role.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbac.common.entity.po.Role;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

/**
 * @program: RoleMapper
 * @description 角色mapper接口
 * @author: 李自豪
 * @create: 2024-08-23 19:07
 **/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据id在 user_role表中查询出id
     * @param id 权限id
     * @return 所拥有的权限数量
     */
    @Select("SELECT COUNT(*) FROM t_user_role WHERE r_id = #{id}")
    int selectCountByRoleId(@Param("id")Long id);

    /**
     * 查询用户对应的所有角色id
     * @param userId 用户Id
     * @return 角色列表
     */
    @Select("SELECT * FROM t_user_role tt where tt.u_id = #{userId}; ")
    List<Long> selectRolesByUserId(@Param("userId")Long userId);

    /**
     * 查询用户对应的所有角色姓名
     * @param userId 用户Id
     * @return 角色列表
     */
    @Select("SELECT role_name FROM t_role role left JOIN t_user_role tt ON role.id = tt.r_id where tt.u_id = #{userId}")
    List<String> selectRoleNamesByRoleId(@Param("userId")Long userId);

    /**
     * 依据roleId删除有所权限
     * @param roleId 角色id
     * @return 删除条数
     */
    @Delete("DELETE FROM t_role_permission WHERE r_id = #{roleId}")
    Long deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 批量插入角色权限关系
     * @param roleId 角色ID
     * @param permissionIds 权限ID集合
     * @return 插入条数
     */
    @Insert({
            "<script>" +
            "INSERT INTO t_role_permission (r_id, p_id) VALUES"+
            "<foreach collection='permissionIds' item='permissionId' separator=','>"+
            "(#{roleId}, #{permissionId})"+
            "</foreach>"+
            "</script>"
    })
    Long insertRolePermission(@Param("roleId") Long roleId, @Param("permissionIds") Set<Long> permissionIds);

    /**
     * 删除对应的所有访问权限
     * @param roleId 角色id
     */
    @Delete("delete from t_role_permission where r_id = #{roleId}")
    void deleteAllPermission(@Param("roleId") Long roleId);
}
