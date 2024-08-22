package com.rbac.role.service;

import com.rbac.common.entity.dto.RoleDTO;
import com.rbac.common.entity.vo.RoleVO;

import java.util.List;
import java.util.Set;

/**
 * Role 业务层
 * @author Lzzh
 */
public interface RoleService  {
    /**
     * 获取所有的角色
     * @return 角色列表
     */
    List<RoleVO> listAll();

    /**
     * 创建角色
     * @param roleDTO 角色
     * @return 1：成功 0：创建失败 -1：角色名称重复
     */
    Integer saveRole(RoleDTO roleDTO);

    /**
     * 修改角色
     * @param roleDTO 角色信息
     * @return 成功
     */
    Boolean updateRoleById(RoleDTO roleDTO);

    /**
     * 删除角色
     * @param id 角色id
     * @return 删除成功
     */
    Boolean deleteRole(Long id);

    /**
     * 获取用户的所有角色
     *
     * @param userId 用户id
     * @return 角色信息
     */
    List<Long> getUserRoles(Long userId);

    /**
     * 根据用户Id查询对应的角色
     * @param userId 用户Id
     * @return 角色姓名
     */
    Set<String> queryRoleNameByUserId(Long userId);

    /**
     * 更新角色的权限
     * @param roleVO 角色信息
     * @return 成功
     */
    Boolean updatePermissions(RoleVO roleVO);

    /**
     * 通过角色名称获取到角色id
     * @param roleNames 角色名称列表
     * @return 角色id
     */
    Set<Long> getRoleIdsByRoleNames(String[] roleNames);

}
