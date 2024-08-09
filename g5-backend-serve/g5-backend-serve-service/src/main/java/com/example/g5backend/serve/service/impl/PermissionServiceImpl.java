package com.example.g5backend.serve.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.g5backend.serve.dao.PermissionDao;
import com.example.g5backend.serve.entity.dto.PermissionDTO;
import com.example.g5backend.serve.entity.po.Permission;
import com.example.g5backend.serve.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-12 11:09
 **/
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements PermissionService {

    @Override
    public List<Permission> findPermissionListByUserId(Long userId) {
        return baseMapper.findPermissionListByUserId(userId);
    }

    @Override
    public String[] findRolesByPermissionId(Long permissionId) {
        return baseMapper.findRolesByPermissionId(permissionId);
    }

    @Override
    public List<Permission> findPermissionList() {
        return baseMapper.selectList(null);
    }

    @Override
    public Long save(PermissionDTO permissionDTO) {
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        this.save(permission);
        return permission.getId();
    }

    @Override
    public boolean updateById(PermissionDTO permissionDTO) {
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        return this.updateById(permission);
    }

    @Override
    public boolean deletePermissionById(Long id) {
        baseMapper.deletePermissionRole(id);
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public boolean savePermissionRoles(Long id, Long[] roleIds) {
        baseMapper.deletePermissionRole(id);
        return baseMapper.savePermissionRoles(id, roleIds);
    }

    @Override
    public Long[] findRoleIdsByRoleNames(String[] roles) {
        Long[] roleIds = new Long[roles.length];
        for (int i = 0; i < roles.length; i++) {
            roleIds[i] = baseMapper.selectRoleIdByRoleName(roles[i]);
        }
        return roleIds;
    }
}
