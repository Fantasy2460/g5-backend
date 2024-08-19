package com.bosssoft.g5backend.serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosssoft.g5backend.serve.config.annotation.ApiIdempotent;
import com.bosssoft.g5backend.serve.entity.po.Role;
import com.bosssoft.g5backend.serve.entity.vo.RoleVO;
import com.bosssoft.g5backend.serve.service.RoleService;
import com.bosssoft.g5backend.serve.service.util.MenuTree;
import com.bosssoft.g5backend.serve.utils.Result;
import com.bosssoft.g5backend.serve.entity.dto.RoleDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @name: RoleController
 * @description 角色控制层
 * @author: 曹鹏翔
 * @create: 2022-08-07 10:26
 **/
@Slf4j
@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "http://localhost:8083")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取所有角色列表
     * @return
     */
    @GetMapping("/getList")
    public Result<List<RoleVO>> getList() {
        List<Role> roleList = roleService.list();
        List<RoleVO> roleVOList = new ArrayList<>();
        for (Role r : roleList) {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(r, roleVO);
            roleVO.setKey(r.getRoleKey());
            roleVO.setName(r.getRoleName());
            roleVO.setRoutes(MenuTree.makeRouter(roleService.findRoutesByRoleId(r.getId()), 0L));
            roleVOList.add(roleVO);
        }
        return Result.ok(roleVOList).message("角色列表查询成功");
    }

    /**
     * 添加角色
     * @param roleVO
     * @param bindingResult
     * @return
     */
    @ApiIdempotent
    @PostMapping("/addRole")
    public Result addRole(@RequestBody @Valid RoleVO roleVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(roleVO, roleDTO);
        int count = (int) roleService.count(new QueryWrapper<Role>()
                .eq("role_name", roleDTO.getName()));
        if (count == 0) {
            roleDTO.setKey(roleDTO.getName());
            roleVO.setId(roleService.save(roleDTO));
            log.info("添加角色成功");
            return Result.ok(roleVO).message("添加角色成功");
        } else {
            log.error("存在重复角色名，添加失败！");
            return Result.error().message("存在重复角色名，添加失败！");
        }
    }

    /**
     * 更新角色信息
     * @param roleVO
     * @param bindingResult
     * @return
     */
    @ApiIdempotent
    @PutMapping("/updateRole")
    public Result updateUser(@RequestBody @Valid RoleVO roleVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(roleVO, roleDTO);
        int count = (int) roleService.count(new QueryWrapper<Role>()
                .eq("role_name", roleDTO.getName())
                .ne("id", roleDTO.getId()));
        if (count == 0) {
            roleService.updateById(roleDTO);
            log.info("修改角色成功");
            return Result.ok(roleVO).message("修改角色成功");
        } else {
            log.error("存在重复角色名，修改失败！");
            return Result.error().message("存在重复角色名，修改失败！");
        }
    }

    /**
     * 为角色分配权限
     * @param roleVO
     * @return
     */
    @ApiIdempotent
    @PutMapping("/updatePermissions")
    public Result updateRolePermissions(@RequestBody RoleVO roleVO) {
        Long[] permissionsId = MenuTree.makePermissionsId(roleVO.getRoutes()).toArray(new Long[0]);
        if (roleService.saveRolePermissions(roleVO.getId(), permissionsId)) {
            return Result.ok().message("权限分配成功");
        }
        return Result.error().message("权限分配失败");
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @ApiIdempotent
    @DeleteMapping("/deleteRole/{id}")
    public Result<Object> deleteRole(@PathVariable @NotBlank Long id) {
        if (!roleService.countUsed(id)) {
            roleService.deleteRoleById(id);
            return Result.ok().message("角色删除成功");
        }
        return Result.error().message("存在用户仍然使用该角色，删除失败");
    }

}
