package com.rbac.user.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rbac.common.annotation.ApiIdempotent;
import com.rbac.common.entity.dto.UserDTO;
import com.rbac.common.entity.po.User;
import com.rbac.common.entity.vo.UserRolesVO;
import com.rbac.common.entity.vo.UserVO;
import com.rbac.common.exception.ServiceException;
import com.rbac.common.util.Result;
import com.rbac.feign.role.RoleClient;
import com.rbac.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: UserController
 * @description 用户控制层，用于管理用户的添加、删除、分配权限
 * @author: 赵佶鑫
 * @create: 2024-08-23 19:32
 **/
@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:8083")
public class UserController {

    /**
     * 用户服务层
     */
    @Resource
    private UserService userService;

    /**
     * 加密实体方法
     */
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    /**
     * 获取所有用户列表
     * @return Result<List<UserVO>>
     */
    @GetMapping("/getList")
    public Result<List<UserVO>> getList() {
        List<User> userList = userService.list();
        //用于存储转化后的UserVO实体
        List<UserVO> userVOList = new ArrayList<>();
        //将User类型转化为UserVO返回
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
     * @return Result
     */
    @ApiIdempotent
    @PostMapping("/addUser")
    public Result addUser(@RequestBody @Valid UserVO userVO, BindingResult bindingResult) {
        UserDTO userDTO = new UserDTO();

        //类型转化
        BeanUtils.copyProperties(userVO, userDTO);

        //校验参数
        if (bindingResult.hasErrors()) {
            String error = bindingResult.getAllErrors().get(0).getDefaultMessage();
            log.error(error);
            return Result.error().message(error);
        }

        //统计数据库中拥有该用户名的用户数量
        int count = (int) userService.count(new QueryWrapper<User>().eq("username", userDTO.getUsername()));
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (count == 0) {

            //符合条件数量为0说明没有重复用户
            userVO.setId(userService.save(userDTO));
            log.info("添加用户成功！");
            return Result.ok(userVO).message("添加用户成功！");
        } else {

            //否则说明用户重复
            log.error("存在重复用户名，添加失败！");
            return Result.error().message("存在重复用户名，添加失败！");
        }
    }

    /**
     * 更新用户信息
     * @param userVO
     * @param bindingResult
     * @return Result
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
     * @return Result<Object>
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
     * @return Result
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
     * @return Result<List<UserRolesVO>>
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
     * @return Result<Object>
     */
    @ApiIdempotent
    @PutMapping("/updateRoles")
    public Result<Object> updateUserRoles(@RequestBody UserRolesVO userRolesVO) {
        if (userService.saveUserRoles(userRolesVO.getId(), Arrays.asList(userRolesVO.getRolesId()))) {
            return Result.ok().message("角色分配成功");
        }
        return Result.error().message("角色分配失败");
    }

    /**
     * 根据username查询用户信息
     * @param username 用户名
     * @return Result
     */
    @GetMapping("/queryUser/{username}")
    Result queryUserByUsername(@PathVariable @NotNull String username) {
        if("".equals(username)) {
            Result.error().code(500);
        }
        User user = userService.findUserByUserName(username);
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if(BeanUtil.isEmpty(userVO)) {
            throw new ServiceException("返回对象为空异常");
        }
        return Result.ok(userVO);
    }

}
