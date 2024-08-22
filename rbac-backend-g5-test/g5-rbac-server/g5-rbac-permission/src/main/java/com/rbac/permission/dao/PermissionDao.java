package com.rbac.permission.dao;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rbac.common.entity.po.Permission;
import com.rbac.permission.dao.mapper.PermissionMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: rbac
 * @description
 * @author: zzzhlee
 * @create: 2022-06-12 10:11
 **/
@Repository
@Transactional(rollbackFor = Exception.class)
public class PermissionDao {

    @Autowired
    PermissionMapper permissionMapper;

    /**
     * 根据用户id获取权限列表
     * @param roleIds 角色列表
     * @return 资源权限列表
     */
    public Set<Permission> findPermissionListByRoleIds(Set<Long> roleIds) {

         return new HashSet<>(permissionMapper.findPermissionListByRoleIds(roleIds));
    }

    /**
     * 根据权限id生成对应角色信息
     * @param permissionId
     * @return
     */
    public String[] findRolesByPermissionId(Long permissionId) {
        if(permissionId == null) {
            return null;
        }
        return permissionMapper.findRolesByPermissionId(permissionId);
    }

    /**
     * 删除角色权限
     * @param id
     */

    public Boolean deletePermissionRole(Long id){
        int amount = permissionMapper.deletePermissionRole(id);
        return  amount > -1;
    }

    /**
     * 保存角色权限
     * @param id
     * @param roleIds
     * @return
     */
    public Boolean savePermissionRoles(Long id, Long[] roleIds) {
        // 删除原本的权限
        for (Long roleId : roleIds) {
            permissionMapper.savePermissionRoles(id, roleId);
        }
        return true;
    }


    public Boolean deleteById(Long id) {
        return permissionMapper.deleteById(id) == 1;
    }

    public Long save(Permission permission) {
        return (long)permissionMapper.insert(permission);
    }

    public int updateById(Permission permission) {
        return permissionMapper.updateById(permission);
    }

    public Long countChildren(Long id) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", id);
        return permissionMapper.selectCount(wrapper);
    }

    public List<Permission> selectAll() {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        return permissionMapper.selectList(queryWrapper);
    }

    public List<Permission> list() {
        return permissionMapper.selectList(null);
    }

    public Set<Permission> getAllChildren(Long id) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("pid", id);
        List<Permission> res = permissionMapper.selectList(wrapper);
        return new HashSet<>(res);
    }
}
