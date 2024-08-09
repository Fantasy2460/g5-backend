package com.example.g5backend.serve.service.util;


import com.example.g5backend.serve.entity.po.Permission;
import com.example.g5backend.serve.entity.vo.RouterVO;
import com.example.g5backend.serve.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @program: rbac
 * @description 根据Permission生成菜单树的工具类
 * @author: 寒旅
 * @create: 2022-06-21 13:54
 **/
@Component
public class MenuTree {

    @Autowired
    private PermissionService permissionService;

    private static MenuTree menuTree;

    @PostConstruct
    void init() {
        menuTree = this;
        menuTree.permissionService = this.permissionService;
    }

    /**
     * 生成路由
     * @param permissionList
     * @param pid
     * @return
     */
    public static List<RouterVO> makeRouter(List<Permission> permissionList, Long pid) {
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
                            menuTree.permissionService.findRolesByPermissionId(item.getId())));
                    List<RouterVO> children = makeRouter(permissionList, item.getId());
                    router.setChildren(children);
                    routerList.add(router);
                });
        return routerList;
    }

    /**
     * 根据前端返回数据生成权限id列表
     * @param routerVOList
     * @return
     */
    public static List<Long> makePermissionsId(List<RouterVO> routerVOList) {
        List<Long> permissionsId = new ArrayList<>();
        for (RouterVO r : routerVOList) {
            permissionsId.add(r.getId());
            if (r.getChildren() != null) {
                List<Long> children = makePermissionsId(r.getChildren());
                for (Long l :
                        children) {
                    permissionsId.add(l);
                }
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
    public static List<Permission> makeMenuTree(List<Permission> menuList, Long pid) {
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
