package com.rbac.feign.role;

import com.rbac.common.entity.po.Role;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * user客户端
 *      暴露对外接口
 * @author Lzzh
 */
@FeignClient(name = "rbac-role", url = "http://localhost:8004/api/role")
public interface RoleClient {
    /**
     * 根据username查询角色
     * @param userId 用户Id
     * @return 角色id
     */
    @GetMapping("/userRoles/{userId}")
    List<Long> queryRoleIdsByUserId(@PathVariable("userId") @NotNull Long userId);

    /**
     * 根据userId询 角色
     * @param userId 用户Id
     * @return 角色id
     */
    @GetMapping("/userRoleName/{userId}")
    Set<String> queryRoleNameByUserId(@PathVariable("userId") @NotNull Long userId);

    /**
     * 获取用户的角色
     * @param userId 用户id
     * @return 角色列表
     */
    @GetMapping("/userRole/{userId}")
    Set<Role> getUserRoles(@PathVariable("userId") @NotNull Long userId);

    /**
     * 根据角色名称获取到角色id
     * @param roleNames 角色名称列表
     * @return ids
     */
    @PostMapping("/getRoleNames")
    Set<Long> queryRoleIdsByRoleNames(String[] roleNames);
}
