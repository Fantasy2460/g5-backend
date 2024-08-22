package com.rbac.permission.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.rbac.common.entity.dto.PermissionDTO;
import com.rbac.common.entity.po.Permission;
import com.rbac.common.entity.vo.PermissionVO;
import com.rbac.common.entity.vo.RouterVO;
import com.rbac.common.exception.ServiceException;
import com.rbac.common.util.Result;
import com.rbac.feign.role.RoleClient;
import com.rbac.permission.dao.PermissionDao;
import com.rbac.permission.service.PermissionService;
import com.rbac.permission.util.MenuTree;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.rmi.ServerException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 操作资源
 * @author zzzhlee
 **/
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class PermissionServiceImpl  implements PermissionService {

    @Autowired
    PermissionDao permissionDao;

    @Resource
    private RoleClient roleClient;

    /**
     * 获取用户对应的资源信息
     * @param userId 用户id
     * @return 资源信息
     */
    @Override
    public Set<Permission> findPermissionListByUserId(Long userId) {
        Set<Long> roleIds = new HashSet<>(roleClient.queryRoleIdsByUserId(userId));
        return permissionDao.findPermissionListByRoleIds(roleIds);
    }

    /**
     * 根据权限查找到角色
     * @param permissionId 权限
     * @return 角色
     */
    @Override
    public String[] findRolesByPermissionId(Long permissionId) {
        return permissionDao.findRolesByPermissionId(permissionId);
    }

    @Override
    public List<RouterVO> searchAll() {
        Set<Permission> permissions = permissionDao.selectAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        return MenuTree.makeRouter(new ArrayList<>(permissions), 0L);
    }

    @Override
    public Long savePermission(PermissionDTO permissionDTO) {
        if(BeanUtil.isEmpty(permissionDTO)) {
            return 0L;
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        Long save = permissionDao.save(permission);
        return save == 1 ? permission.getId() : -1L;
    }

    @Override
    public Boolean updatePermission(PermissionDTO permissionDTO) {
        if(BeanUtil.isEmpty(permissionDTO)) {
            return false;
        }
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        return permissionDao.updateById(permission) == 1;
    }

    @Override
    public Boolean deletePermissionById(Long id) {
        if(BeanUtil.isEmpty(id)) {
            return false;
        }
        return permissionDao.deleteById(id);
    }

    @Override
    public Boolean savePermissionRoles(Long id, Long[] roleIds) {
        if(id == null || roleIds == null) {
            throw new ServiceException("业务层传入参数异常");
        }
        return permissionDao.savePermissionRoles(id, roleIds);
    }

    @Override
    public Long[] findRoleIdsByRoleNames(String[] roleNames) {
        if(roleNames == null || roleNames.length == 0) {
            return null;
        }
        Set<Long> res = roleClient.queryRoleIdsByRoleNames(roleNames);
        if(res == null || res.isEmpty()) {
            return null;
        }
        return res.toArray(new Long[0]);
    }

    @Override
    public Boolean removePermission(Long id) {
        if(id == null) {
            throw new ServiceException("service层传入参数异常");
        }
        /*判断是否存在子部门 存在无法删除*/
        long amount = permissionDao.countChildren(id);
        if(amount != 0) {
            return false;
        }
        if (Boolean.TRUE.equals(permissionDao.deleteById(id))) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateRoles(PermissionVO permissionVO) {
        /**
         * todo
         *      1、从redis中 依据 roleName 取出roleId
         *      2、根据permission_id 删除 role_permission表中所有的role_id
         *      3、重新添加 role_id
         */
        Long[] roleIds = findRoleIdsByRoleNames(permissionVO.getRoles());

        if (Boolean.FALSE.equals(permissionDao.deletePermissionRole(permissionVO.getId()))) {
            throw new ServiceException("持久层操作异常，删除失败");
        }
        return CollectionUtils.isEmpty(Arrays.asList(roleIds)) ? true
                : permissionDao.savePermissionRoles(permissionVO.getId(), roleIds);

    }



    @Override
    public  List<RouterVO> makeRouter(List<Permission> permissionList, Long pid) {
        List<RouterVO> routerList = new ArrayList<>();
        Optional.ofNullable(permissionList).orElse(new ArrayList<>())
                .stream().filter(item -> item != null && Objects.equals(item.getPid(), pid))
                .forEach(item -> {
                    RouterVO router = new RouterVO();
                    router.setId(item.getId());
                    router.setPath(item.getPath());
                    if (item.getPid() == 0L) {
                        router.setComponent(item.getComponent());
                        router.setAlwaysShow(true);
                    } else {
                        router.setComponent(item.getComponent());
                        router.setAlwaysShow(false);
                    }
                    router.setMeta(new RouterVO.Meta(item.getTitle(),
                            item.getIcon(),
                            findRolesByPermissionId(item.getId())));
                    List<RouterVO> children = makeRouter(permissionList, item.getId());
                    router.setChildren(children);
                    routerList.add(router);
                });
        return routerList;
    }

    @Override
    public List<Permission> listAll() {
        return permissionDao.list();

    }

    @Override
    public Set<Permission> getChildrenResource(Long id) {
        return permissionDao.getAllChildren(id);
    }

    /**
     * 根据前端返回数据生成权限id列表
     * @param routerVOList 路由表
     * @return 路由信息
     */
    private   List<Long> makePermissionsId(List<RouterVO> routerVOList) {
        List<Long> permissionsId = new ArrayList<>();
        for (RouterVO r : routerVOList) {
            permissionsId.add(r.getId());
            if (r.getChildren() != null) {
                List<Long> children = makePermissionsId(r.getChildren());
                permissionsId.addAll(children);
            }
        }
        return permissionsId;
    }

    /**
     * 生成菜单树
     * @param menuList
     * @param pid
     * @return
     */
    private List<Permission> makeMenuTree(List<Permission> menuList, Long pid) {
        List<Permission> permissionList = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream().filter(item -> item != null && Objects.equals(item.getPid(), pid))
                .forEach(item -> {
                    Permission permission = new Permission();
                    BeanUtils.copyProperties(item, permission);
                    List<Permission> children = makeMenuTree(menuList, item.getId());
                    permission.setChildren(children);
                    permissionList.add(permission);
                });
        return permissionList;
    }

}
