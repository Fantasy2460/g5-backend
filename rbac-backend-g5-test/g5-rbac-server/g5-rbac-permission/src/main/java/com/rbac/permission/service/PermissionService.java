package com.rbac.permission.service;


import com.rbac.common.entity.dto.PermissionDTO;
import com.rbac.common.entity.po.Permission;
import com.rbac.common.entity.vo.PermissionVO;
import com.rbac.common.entity.vo.RouterVO;

import java.util.List;
import java.util.Set;


/**
 * @program: rbac
 * @description
 * @author: zzzhlee
 * @create: 2022-06-12 11:07
 **/
public interface PermissionService {

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId
     * @return
     */
    Set<Permission> findPermissionListByUserId(Long userId);

    /**
     * 根据权限id生成对应角色信息
     * @param permissionId
     * @return
     */
    String[] findRolesByPermissionId(Long permissionId);

    /**
     * 查询菜单列表
     *
     * @return
     */
    List<RouterVO> searchAll();

    /**
     * 添加方法
     * @param permissionDTO
     * @return
     */
    Long savePermission(PermissionDTO permissionDTO);

    /**
     * 更新方法
     *
     * @param permissionDTO
     * @return
     */
    Boolean updatePermission(PermissionDTO permissionDTO);

    /**
     * 删除方法
     *
     * @param id
     * @return
     */
    Boolean deletePermissionById(Long id);

    /**
     * 权限分配角色
     *
     * @param id
     * @param roleIds
     * @return
     */
    Boolean savePermissionRoles(Long id, Long[] roleIds);

    /**
     * 根据角色名返回角色id集合
     * @param roles
     * @return
     */
    Long[] findRoleIdsByRoleNames(String[] roles);

    /**
     * 删除部门
     * @param id 部门id
     * @return 成功？
     */
    Boolean removePermission(Long id);

    /**
     * 更新roles
     * @param permissionVO
     * @return
     */
    Boolean updateRoles(PermissionVO permissionVO);

    /**
     * 生成菜单路由
     * @param permissionList 菜单列表
     * @param pid 父id
     * @return 路由菜单
     */
    List<RouterVO> makeRouter(List<Permission> permissionList, Long pid);

    /**
     * 展示全部资源
     * @return 资源列表
     */
    List<Permission> listAll();

    /**
     * 获取子资源信息
     * @param id 父id
     * @return 子资源信息列表
     */
    Set<Permission> getChildrenResource(Long id);
}
