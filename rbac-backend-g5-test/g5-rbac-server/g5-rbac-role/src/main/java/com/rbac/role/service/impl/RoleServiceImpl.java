package com.rbac.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.rbac.common.entity.dto.RoleDTO;
import com.rbac.common.entity.po.Permission;
import com.rbac.common.entity.po.Role;
import com.rbac.common.entity.vo.RoleVO;
import com.rbac.common.entity.vo.RouterVO;
import com.rbac.common.exception.Message;
import com.rbac.common.exception.ServiceException;
import com.rbac.common.exception.handler.GlobalExceptionHandler;
import com.rbac.common.exception.handler.ServiceExceptionHandler;
import com.rbac.feign.permiss.PermissionClient;
import com.rbac.role.dao.RoleDao;
import com.rbac.role.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 业务层——处理角色的业务
 * @author Lzzh
 * @version 1.0
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class RoleServiceImpl implements RoleService {

    Message message;

    @Autowired
    RoleDao roleDao;

    @Resource
    private PermissionClient permissionClient;


    @Override
    public List<RoleVO> listAll() {
        List<Role> roles = roleDao.selectAll();
        return roles.stream()
                .map(obj->{
                    RoleVO vo = new RoleVO();
                    BeanUtil.copyProperties(obj,vo);
                    vo.setKey(obj.getRoleKey());
                    vo.setName(obj.getRoleName());
                    return vo;
                }).collect(Collectors.toList());

    }

    @Override
    public Integer saveRole(RoleDTO roleDTO) {
        if(BeanUtil.isEmpty(roleDTO)) {
            throw new ServiceException(message.paramPassErr());
        }
        Role role = BeanUtil.copyProperties(roleDTO, Role.class);
        BeanUtils.copyProperties(roleDTO, role);
        role.setRoleName(roleDTO.getName());
        role.setRoleKey(roleDTO.getKey());
        Integer res = roleDao.insertIfAbsent(role);
        if(res == null) {
            throw new ServiceException(message.persistentInsert());
        }
        if(res != 1 && res != -1) {
            throw new ServiceException(message.persistentInsert());
        }
        return res;
    }

    @Override
    public Boolean updateRoleById(RoleDTO roleDTO) {
        if(roleDTO == null) {
            throw new ServiceException(message.paramPassErr());
        }
        Integer res = roleDao.updateById(roleDTO);
        if(res == null || res == 0) {
            throw new ServiceException(message.persistentInsert());
        }
        return res == 1;
    }

    @Override
    public Boolean deleteRole(Long id) {
        if(BeanUtil.isEmpty(id)){
            throw new ServiceException(message.paramPassErr());
        }
        return roleDao.delete(id);
    }

    @Override
    public List<Long> getUserRoles(Long userId) {
        if(BeanUtil.isEmpty(userId)) {
            throw new ServiceException(message.paramPassErr());
        }
        return roleDao.getRolesByUserId(userId);

    }

    @Override
    public Set<String> queryRoleNameByUserId(Long userId) {
        return roleDao.getRoleNamesByUserId(userId);
    }

    @Override
    public Boolean updatePermissions(RoleVO roleVO) {
        if(BeanUtil.isEmpty(roleVO)) {
            throw  new ServiceException(message.getBusinessParamPassErr());
        }
        Long roleId = roleVO.getId();
        // 获取到权限id
        List<RouterVO> list = roleVO.getRoutes();
        Set<Long> newPermissionIds = new HashSet<>();

        for (RouterVO routerVO : list) {
            newPermissionIds.add(routerVO.getId());
            if(routerVO.getChildren() != null) {
                for (RouterVO r : routerVO.getChildren()) {
                    newPermissionIds.add(r.getId());
                }
            } else {
                Set<Permission> resources = permissionClient.getChildrenResource(routerVO.getId());
                if(resources != null && !resources.isEmpty()) {
                    Set<Long> set = resources.stream().map(Permission::getId).collect(Collectors.toSet());
                    newPermissionIds.addAll(set);
                }
            }
        }
        // 删除原本的权限
        roleDao.deleteAllPermission(roleId);
        // 增加新权限
        return roleDao.insertAllPermission(roleId, newPermissionIds);
    }

    @Override
    public Set<Long> getRoleIdsByRoleNames(String[] roleNames) {
        if(roleNames == null || roleNames.length == 0) {
            return null;
        }
        return roleDao.getRoleIdsByRoleNames(roleNames);
    }

    Set<Long> getRoleIdsFromRoleVo(List<RouterVO> routerVOList) {
        Set<Long> res = new HashSet<>();
       Deque<RouterVO> deque = new LinkedList<>(routerVOList);
       while(!deque.isEmpty()) {
           RouterVO pop = deque.pop();
           res.add(pop.getId());
           if(pop.getChildren() != null) {
               for (int i = 0; i < pop.getChildren().size(); i++) {
                   deque.push(pop.getChildren().get(i));
               }
           }
       }
        return res;
    }
}
