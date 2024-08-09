package com.example.g5backend.serve.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.g5backend.serve.entity.po.Permission;
import com.example.g5backend.serve.entity.po.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-13 20:21
 **/
@Mapper
public interface RoleDao extends BaseMapper<Role> {

    /**
     * 根据用户id返回对应角色列表
     * @param roleId
     * @return
     */
    List<Permission> selectRouteByRoleId(Long roleId);

    /**
     * 删除角色权限
     * @param id
     * @return
     */
    @Delete("DELETE FROM t_role_permission WHERE r_id = #{id}")
    int deleteRoleRoutes(Long id);

    /**
     * 保存角色权限
     * @param id
     * @param permissionsId
     * @return
     */
    int saveRoleRoutes(Long id, Long[] permissionsId);

    /**
     * 查询正在使用角色的用户数量
     * @param id
     * @return
     */
    @Select("SELECT COUNT(*) FROM t_user_role WHERE r_id = #{id}")
    int countUsed(Long id);
}
