package com.rbac.permission.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.rbac.common.annotation.ApiIdempotent;
import com.rbac.common.entity.dto.PermissionDTO;
import com.rbac.common.entity.po.Permission;
import com.rbac.common.entity.vo.PermissionVO;
import com.rbac.common.entity.vo.RouterVO;
import com.rbac.common.exception.ServiceException;
import com.rbac.common.util.Result;
import com.rbac.common.util.ResultCode;
import com.rbac.permission.service.PermissionService;
import com.rbac.permission.util.MenuTree;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

/**
 * @program: PermissionController
 * @description 系统子菜单控制层，用于管理子菜单的添加、角色访问权限分配等
 * @author: 李自豪
 * @create: 2024-08-23 19:07
 **/
@Api(tags = "swagger接口")
@Slf4j
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    /**
     * 子菜单服务层
     */
    @Resource
    private PermissionService permissionService;



    /**
     * 请求所有路由列表
     * @return 路由列表
     */
    @GetMapping("/getList")
    public Result<List<RouterVO>> getList() {

        return Result.ok(permissionService.searchAll()).code(ResultCode.SUCCESS);
    }

    /**
     * 添加权限
     * @param permissionVO 资源信息
     * @param bindingResult 异常信息
     * @return 新增成功？
     */
    @ApiIdempotent
    @PostMapping("/addPermission")
    public Result addPermission(@RequestBody @Valid PermissionVO permissionVO,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(permissionVO, permissionDTO);
        permissionDTO.setCode(permissionVO.getTitle().toLowerCase());
        Long id = permissionService.savePermission(permissionDTO);
        if(-1 == id.intValue()) {
            log.info("添加权限成功！");
            return Result.error().message("增加权限失败！");
        }
        permissionVO.setId(id);
        log.info("添加权限成功！");
        return Result.ok(permissionVO).message("添加权限成功！");
    }

    /**
     * 更新资源权限信息
     * @param permissionVO 资源信息
     * @param bindingResult 异常情况
     * @return 添加结果
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
        if (Boolean.TRUE.equals(permissionService.updatePermission(permissionDTO))) {
            log.info("修改权限成功！");
            return Result.ok(permissionVO).message("修改权限成功！");
        } else {
            log.error("修改权限失败!");
            return Result.error().message("修改权限失败！");
        }
    }

    /**
     * 删除权限
     * @param id 权限ID
     * @return 删除结果
     */
    @ApiIdempotent
    @DeleteMapping("/deletePermission/{id}")
    public Result deletePermission(@PathVariable("id") @NotBlank Long id) {
        if(BeanUtil.isEmpty(id)) {
            throw new ServiceException("接受参数出现异常");
        }
        if (BooleanUtil.isTrue(permissionService.removePermission(id))) {
            return Result.ok().message("权限删除成功");
        }
        return Result.error().message("该权限存在子权限,权限删除失败");
    }

    /**
     * 更新角色的 对应权限
     * @param permissionVO 更新角色 权限
     * @return 更新结果
     */
    @PutMapping("/updateRoles")
    @ApiIdempotent
    public Result updateRoles(@RequestBody PermissionVO permissionVO) {
        if(permissionVO == null) {
            return Result.error().message("请填写内容");
        }
        if (Boolean.TRUE.equals(permissionService.updateRoles(permissionVO))) {
            return Result.ok().code(ResultCode.SUCCESS).message("分配成功");
        }

        return Result.error().message("角色分配失败");
    }

    @GetMapping("/getPermission/{userId}")
    public Set<Permission> findResourcesByUserId(@PathVariable("userId") @NotNull Long userId) {
        return permissionService.findPermissionListByUserId(userId);
    }


    /**
     * 生成路由
     * @param userId 用户id
     * @return 表单
     */
    @ApiIdempotent
    @GetMapping("/makeRouter/{userId}")
    public Result<List<RouterVO>> makeRouter(@PathVariable("userId") Long userId) {
        if(BeanUtil.isEmpty(userId)) {
            throw new ServiceException("传入参数为null");
        }
        List<Permission> permissions = permissionService.listAll();

        if(permissions == null || permissions.isEmpty()) {
            return Result.error();
        }
        List<RouterVO> list = MenuTree.makeRouter(permissions, 0L);
        if(list.isEmpty()) {
            throw new ServiceException("服务层返回数据异常");
        }
        return Result.ok(list).code(ResultCode.SUCCESS).message("成功");
    }

    /**
     * 获取全部子资源
     * @param id 父资源id
     * @return 子资源列表
     */
    @GetMapping("/getChildren/{id}")
    Set<Permission> getChildrenResource(@PathVariable("id") Long id) {
        if(id == null) {
            return null;
        }
        return permissionService.getChildrenResource(id);
    }

}
