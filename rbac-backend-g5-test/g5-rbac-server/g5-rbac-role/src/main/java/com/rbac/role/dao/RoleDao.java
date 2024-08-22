package com.rbac.role.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rbac.common.entity.dto.RoleDTO;
import com.rbac.common.entity.po.Role;
import com.rbac.common.exception.ServiceException;
import com.rbac.role.dao.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * @program: RoleDao
 * @description 角色dao层实现
 * @author: 李自豪
 * @create: 2024-08-23 19:07
 **/
@Repository
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class RoleDao {
    /**
     * 角色mapper接口
     */
    @Resource
    RoleMapper mapper;

    /**
     * 查询所有的角色列表
     * @return 角色列表
     */
    public List<Role> selectAll() {
        return mapper.selectList(new QueryWrapper<>());
    }

    /**
     * 新建角色
     * @param role
     * @return 是否成功
     */
    public Integer insertIfAbsent(Role role) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", role.getRoleName());
        if (mapper.selectCount(wrapper) != 0) {
            return -1;
        }
        return mapper.insert(role);
    }

    /**
     * 通过角色id更新角色信息
     * @param roleDTO
     * @return 是否成功
     */
    public Integer updateById(RoleDTO roleDTO) {

        if (Boolean.TRUE.equals(this.hasRepeatName(roleDTO))) {
            return -1;
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setRoleKey(roleDTO.getKey());
        role.setRoleName(roleDTO.getName());
        return mapper.updateById(role);
    }
    /**
     * 查验是否存在重复role_name
     * @param roleDTO role
     * @return 存在为true
     */
    private Boolean hasRepeatName(RoleDTO roleDTO) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.eq("role_name", roleDTO.getName())
                .ne("id", roleDTO.getId());
        return mapper.selectCount(wrapper) != 0;
    }

    /**
     * 删除角色
     * @param id 角色id
     * @return 成功
     */
    public Boolean delete(Long id) {
        if (mapper.selectCountByRoleId(id) > 0) {
            return false;
        }
        return mapper.deleteById(id) == 1;
    }

    /**
     * 获取到用户对应的角色
     * @param userId 用户id
     * @return 角色信息
     */
    public List<Long> getRolesByUserId(Long userId) {
        return mapper.selectRolesByUserId(userId);
    }

    /**
     * 获取所有的角色名称
     * @param userId 用户
     * @return 角色id
     */
    public Set<String> getRoleNamesByUserId(Long userId) {
        return new HashSet<>(mapper.selectRoleNamesByRoleId(userId));
    }

    /**
     * 更新角色的权限
     * @param roleId 角色
     * @param permissionIds 权限
     * @return 成功？
     */
    public Boolean updateRolePermissions(Long roleId, Set<Long> permissionIds) {
        mapper.deleteByRoleId(roleId);
        Long amount = mapper.insertRolePermission(roleId, permissionIds);
        try{
            if(amount != permissionIds.size()) {
                throw new ServiceException("数据库插入异常");
            }
            return true;
        } catch(Exception e) {
            log.error("更新信息出现异常");
            return false;
        }
    }

    /**
     * 通过roleKey 获取到roleId
     * @param roleKeys roleKey数组
     * @return roleIds
     */
    public Set<Long> getRoleIdsByRoleNames(String[] roleKeys) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        wrapper.in("role_key", roleKeys);
        List<Role> roles = mapper.selectList(wrapper);
        return roles.stream().map(Role::getId).collect(Collectors.toSet());
    }

    /**
     * 删除该角色对应的全部访问权限
     * @param roleId 角色id
     * @return 删除成功？
     */
    public Boolean deleteAllPermission(Long roleId) {
        mapper.deleteAllPermission(roleId);
        return true;
    }

    /**
     * 向表中增加所有新权限
     * @param newPermissionIds 新权限Id
     * @return 成功？
     */
    public Boolean insertAllPermission(Long roleId,Set<Long> newPermissionIds) {
        try {
            Long amount = mapper.insertRolePermission(roleId, newPermissionIds);
            if(amount != newPermissionIds.size()) {
                throw  new ServiceException("分配资源失败");
            }
            return true;
        } catch (Exception e) {
            log.info("分配资源失败");
            return false;
        }
    }
}
