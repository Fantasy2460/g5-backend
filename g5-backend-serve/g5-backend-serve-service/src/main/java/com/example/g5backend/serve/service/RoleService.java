package com.example.g5backend.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.g5backend.serve.entity.dto.RoleDTO;
import com.example.g5backend.serve.entity.po.Permission;
import com.example.g5backend.serve.entity.po.Role;

import java.util.List;

/**
 * @name: RoleService
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 15:21
 **/
public interface RoleService extends IService<Role> {

    /**
     * 添加方法
     * @param roleDTO
     * @return
     */
    Long save(RoleDTO roleDTO);

    /**
     * 修改方法
     * @param roleDTO
     * @return
     */
    boolean updateById(RoleDTO roleDTO);

    /**
     * 根据用户id返回对应权限列表
     * @param roleId
     * @return
     */
    List<Permission> findRoutesByRoleId(Long roleId);

    /**
     * 为角色分配权限
     * @param id
     * @param permissionsId
     * @return
     */
    boolean saveRolePermissions(Long id, Long[] permissionsId);

    /**
     * 根据id删除角色
     * @param id
     * @return
     */
    boolean deleteRoleById(Long id);

    /**
     * 查询是否存在用户使用角色
     * @param id
     * @return
     */
    boolean countUsed(Long id);
}
