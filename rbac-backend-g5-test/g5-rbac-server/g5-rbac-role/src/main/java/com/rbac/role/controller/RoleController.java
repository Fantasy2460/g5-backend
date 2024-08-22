package com.rbac.role.controller;

import cn.hutool.core.util.BooleanUtil;
import com.rbac.common.entity.dto.RoleDTO;
import com.rbac.common.entity.po.Role;
import com.rbac.common.entity.vo.RoleVO;
import com.rbac.common.util.Result;
import com.rbac.common.util.ResultCode;
import com.rbac.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: SysUserController
 * @description 角色控制层，用于管理角色的增加、删除、对应权限
 * @author: 李自豪
 * @create: 2024-08-23 19:07
 **/
@Slf4j
@RestController
@RequestMapping("/api/role")
public class RoleController {

    /**
     * 角色服务层
     */
    @Resource
    private RoleService roleService;

    /**
     * 获取所有角色列表
     * @return 角色列表
     */
    @GetMapping("/getList")
    public Result<List<RoleVO>> getList() {
        List<RoleVO> roleList = roleService.listAll();
        if(roleList == null || roleList.isEmpty()) {
            return Result.error();
        }
        return Result.ok(roleList).code(ResultCode.SUCCESS).message("角色列表查询成功");
    }

    /**
     * 添加角色
     * @param roleVO  角色
     * @param bindingResult 异常信息
     * @return 操作结果
     */
    @PostMapping("/addRole")
    public Result addRole(@RequestBody @Valid RoleVO roleVO, BindingResult bindingResult)  {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(roleVO, roleDTO);

        Integer res = roleService.saveRole(roleDTO);

        if(res == -1) {
            return Result.error().message("存在重复角色名，添加失败");
        } else {
            return Result.ok(roleVO).message("角色添加成功");
        }

    }

    /**
     * 更新角色信息
     * @param roleVO 角色信息
     * @param bindingResult 异常的信息
     * @return result
     */
    @PutMapping("/updateRole")
    public Result updateRole(@RequestBody @Valid RoleVO roleVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(roleVO, roleDTO);

        if (BooleanUtil.isTrue(roleService.updateRoleById(roleDTO))) {
            log.info("修改角色成功");
            return Result.ok(roleVO).message("修改角色成功");
        } else {
            log.error("存在重复角色名，修改失败！");
            return Result.error().message("存在重复角色名，修改失败！");
        }
    }

    /**
     * 为角色分配权限
     * @param roleVO 权限
     * @return 分配是否成功
     */
    @PutMapping("/updatePermissions")
    public Result updatePermissionsOfRole(@RequestBody RoleVO roleVO) {
        if(roleVO == null) {
            Result.error().message("传入参数异常");
        }
        if (Boolean.FALSE.equals(roleService.updatePermissions(roleVO))) {
            return Result.error().message("权限分配失败");
        }
        return Result.ok(ResultCode.SUCCESS).message("权限更新成功");

    }

    /**
     * 删除角色
     * @param id 角色id
     * @return 删除是否成功
     */
    @DeleteMapping("/deleteRole/{id}")
    public Result<Object> deleteRole(@PathVariable @NotBlank Long id) {

        if(Boolean.TRUE.equals(roleService.deleteRole(id))) {
            return Result.ok().message("角色删除成功");
        }
        return Result.error().message("存在用户仍然使用该角色，删除失败");
    }

    /**
     * 通过用户id查询用户所对应的角色
     * @param userId 角色id
     * @return 查询
     */
    @GetMapping("/userRoles/{userId}")
    public List<Long> queryRoleIdsByUserId(@PathVariable("userId") @NotNull Long userId) {
        return roleService.getUserRoles(userId);
    }


    /**
     * 根据userId询 角色
     * @param userId 用户Id
     * @return 角色id
     */
    @GetMapping("/userRoleName/{userId}")
    public Set<String> queryRoleNameByUserId(@PathVariable("userId") @NotNull Long userId) {
        return roleService.queryRoleNameByUserId(userId);
    }

    /**
     * 根据userId询 角色
     * @param roleNames 用户Id
     * @return 角色id列表
     */
    @PostMapping("/getRoleNames")
    Set<Long> queryRoleIdsByRoleNames(@RequestBody String[] roleNames) {
        return roleService.getRoleIdsByRoleNames(roleNames);
    }
}

