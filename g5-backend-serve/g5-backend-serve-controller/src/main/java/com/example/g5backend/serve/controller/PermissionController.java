package com.example.g5backend.serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosssoft.g5backend.serve.config.annotation.ApiIdempotent;
import com.example.g5backend.serve.entity.dto.PermissionDTO;
import com.example.g5backend.serve.entity.po.Permission;
import com.example.g5backend.serve.entity.vo.PermissionVO;
import com.example.g5backend.serve.entity.vo.RouterVO;
import com.example.g5backend.serve.service.PermissionService;
import com.example.g5backend.serve.service.util.MenuTree;
import com.example.g5backend.serve.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-22 10:10
 **/
@Slf4j
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 请求所有路由列表
     * @return
     */
    @GetMapping("/getList")
    public Result<List<RouterVO>> getList() {
        List<Permission> permissionList = permissionService.findPermissionList();
        List<Permission> collect = permissionList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<RouterVO> routerVOList = MenuTree.makeRouter(collect, 0L);
        return Result.ok(routerVOList).message("查询权限列表成功");
    }

    /**
     * 添加用户
     * @param permissionVO
     * @param bindingResult
     * @return
     */
    @ApiIdempotent
    @PostMapping("/addPermission")
    public Result addPermission(@RequestBody @Valid PermissionVO permissionVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(permissionVO, permissionDTO);
        permissionDTO.setCode(permissionVO.getTitle().toLowerCase());
        permissionVO.setId(permissionService.save(permissionDTO));
        log.info("添加权限成功！");
        return Result.ok(permissionVO).message("添加权限成功！");
    }

    /**
     * 更新权限
     * @param permissionVO
     * @param bindingResult
     * @return
     */
    @ApiIdempotent
    @PutMapping("/updatePermission")
    public Result updatePermission(@RequestBody @Valid PermissionVO permissionVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(permissionVO, permissionDTO);
        if (permissionService.updateById(permissionDTO)) {
            log.info("修改权限成功！");
            return Result.ok(permissionVO).message("修改权限成功！");
        } else {
            log.error("修改权限失败!");
            return Result.error().message("修改权限失败！");
        }
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @ApiIdempotent
    @DeleteMapping("/deletePermission/{id}")
    public Result deletePermission(@PathVariable @NotBlank Long id) {
        // 判断是否存在子部门 存在无法删除
        long pid = permissionService.count(new QueryWrapper<Permission>().eq("pid", id));
        if (pid == 0) {
            permissionService.deletePermissionById(id);
            return Result.ok().message("权限删除成功");
        }
        return Result.error().message("该权限存在子权限,权限删除失败");
    }

    @PutMapping("/updateRoles")
    @ApiIdempotent
    public Result updateRoles(@RequestBody PermissionVO permissionVO) {
        Long[] roleIds = permissionService.findRoleIdsByRoleNames(permissionVO.getRoles());
        if (permissionService.savePermissionRoles(permissionVO.getId(), roleIds)) {
            return Result.ok().message("角色分配成功");
        }
        return Result.error().message("角色分配失败");
    }

}
