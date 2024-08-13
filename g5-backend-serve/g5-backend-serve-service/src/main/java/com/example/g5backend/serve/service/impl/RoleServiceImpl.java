package com.example.g5backend.serve.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.g5backend.serve.dao.RoleDao;
import com.example.g5backend.serve.entity.dto.RoleDTO;
import com.example.g5backend.serve.entity.po.Permission;
import com.example.g5backend.serve.entity.po.Role;
import com.example.g5backend.serve.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @name: RoleServiceImpl
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 15:21
 **/
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Override
    public Long save(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setRoleName(roleDTO.getName());
        role.setRoleKey(roleDTO.getKey());
        this.save(role);
        return role.getId();
    }

    @Override
    public boolean updateById(RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setRoleKey(roleDTO.getKey());
        role.setRoleName(roleDTO.getName());
        return this.updateById(role);
    }

    @Override
    public List<Permission> findRoutesByRoleId(Long roleId) {
        return baseMapper.selectRouteByRoleId(roleId);
    }

    @Override
    public boolean saveRolePermissions(Long id, Long[] permissionsId) {
        baseMapper.deleteRoleRoutes(id);
        return baseMapper.saveRoleRoutes(id, permissionsId) > 0;
    }

    @Override
    public boolean deleteRoleById(Long id) {
        baseMapper.deleteRoleRoutes(id);
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public boolean countUsed(Long id) {
        return baseMapper.countUsed(id) > 0;
    }
}
