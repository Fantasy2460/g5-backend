package com.example.g5backend.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.g5backend.serve.entity.dto.PermissionDTO;
import com.example.g5backend.serve.entity.po.Permission;

import java.util.List;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-12 11:07
 **/
public interface PermissionService extends IService<Permission> {

    /**
     * 根据用户ID查询权限列表
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
     * 查询菜单列表
     * @return
     */
    List<Permission> findPermissionList();

    /**
     * 添加方法
     * @param permissionDTO
     * @return
     */
    Long save(PermissionDTO permissionDTO);

    /**
     * 更新方法
     * @param permissionDTO
     * @return
     */
    boolean updateById(PermissionDTO permissionDTO);

    /**
     * 删除方法
     * @param id
     * @return
     */
    boolean deletePermissionById(Long id);

    /**
     * 权限分配角色
     * @param id
     * @param roleIds
     * @return
     */
    boolean savePermissionRoles(Long id, Long[] roleIds);

    /**
     * 根据角色名返回角色id集合
     * @param roles
     * @return
     */
    Long[] findRoleIdsByRoleNames(String[] roles);
}
