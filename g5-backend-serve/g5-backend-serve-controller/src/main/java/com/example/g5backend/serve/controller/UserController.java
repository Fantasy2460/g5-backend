package com.example.g5backend.serve.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bosssoft.g5backend.serve.config.annotation.ApiIdempotent;
import com.example.g5backend.serve.entity.dto.UserDTO;
import com.example.g5backend.serve.entity.po.User;
import com.example.g5backend.serve.entity.vo.UserRolesVO;
import com.example.g5backend.serve.entity.vo.UserVO;
import com.example.g5backend.serve.service.UserService;
import com.example.g5backend.serve.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: rbac
 * @description 用户控制层
 * @author: 寒旅
 * @create: 2022-06-10 18:50
 **/
@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:8083")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    /**
     * 获取所有用户列表
     * @return
     */
    @GetMapping("/getList")
    public Result<List<UserVO>> getList() {
        List<User> userList = userService.list();
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            UserVO userVO = new UserVO();
            userVO.clone(user);
            userVOList.add(userVO);
        }
        log.info("获取用户列表成功");
        return Result.ok(userVOList);
    }

    /**
     * 添加用户
     * @param userVO
     * @param bindingResult
     * @return
     */
    @ApiIdempotent
    @PostMapping("/addUser")
    public Result addUser(@RequestBody @Valid UserVO userVO, BindingResult bindingResult) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userVO, userDTO);
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }
        int count = (int) userService.count(new QueryWrapper<User>().eq("username", userDTO.getUsername()));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (count == 0) {

            userVO.setId(userService.save(userDTO));
            log.info("添加用户成功！");
            return Result.ok(userVO).message("添加用户成功！");
        } else {
            log.error("存在重复用户名，添加失败！");
            return Result.error().message("存在重复用户名，添加失败！");
        }
    }

    /**
     * 更新用户信息
     * @param userVO
     * @param bindingResult
     * @return
     */
    @ApiIdempotent
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody @Valid UserVO userVO, BindingResult bindingResult) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userVO, userDTO);
        // 数据校验 存在格式错误返回错误信息
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }
        // 是否存在相同用户名 且不是自身
        int count = (int) userService.count(new QueryWrapper<User>()
                .eq("username", userDTO.getUsername())
                .ne("id", userDTO.getId()));
        if (count == 0) {
            userService.updateById(userDTO);
            log.info("修改用户成功！");
            return Result.ok(userVO).message("修改用户成功！");
        } else {
            log.error("存在相同用户名的用户，修改用户信息失败!");
            return Result.error().message("存在相同用户名的用户，修改用户信息失败!");
        }
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @ApiIdempotent
    @DeleteMapping("/deleteUser/{id}")
    public Result<Object> deleteUser(@PathVariable @NotBlank Long id) {
        if (userService.deleteUserById(id)) {
            return Result.ok().message("用户删除成功");
        }
        return Result.error().message("用户删除失败");
    }

    /**
     * 按用户名模糊查询
     * @param username
     * @return
     */
    @GetMapping("/queryUser")
    public Result queryUser(String username) {
        List<UserVO> userVOList = userService.findUsersByUserName(username);
        if (userVOList.isEmpty()) {
            return Result.error().message("没有符合条件数据");
        }
        return Result.ok(userVOList).message("用户查询成功");
    }

    /**
     * 获取所有用户的角色列表
     * @return
     */
    @GetMapping("/roles")
    public Result<List<UserRolesVO>> getAllUserRoles() {
        List<User> userList = userService.list();
        List<UserRolesVO> userRolesVOList = new ArrayList<>();
        for (User user : userList) {
            UserRolesVO vo = new UserRolesVO();
            BeanUtils.copyProperties(user, vo);
            vo.setRolesId(userService.findRolesIdByUserId(user.getId()));
            vo.setRoles(userService.findRolesByUserId(user.getId()));
            userRolesVOList.add(vo);
        }
        return Result.ok(userRolesVOList);
    }

    /**
     * 为用户分配角色
     * @param userRolesVO
     * @return
     */
    @ApiIdempotent
    @PutMapping("/updateRoles")
    public Result<Object> updateUserRoles(@RequestBody UserRolesVO userRolesVO) {
        if (userService.saveUserRoles(userRolesVO.getId(), Arrays.asList(userRolesVO.getRolesId()))) {
            return Result.ok().message("角色分配成功");
        }
        return Result.error().message("角色分配失败");
    }
}
