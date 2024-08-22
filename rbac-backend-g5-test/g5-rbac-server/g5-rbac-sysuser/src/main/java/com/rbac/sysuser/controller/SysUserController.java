package com.rbac.sysuser.controller;


import cn.hutool.core.bean.BeanUtil;
import com.rbac.common.entity.dto.UserDTO;
import com.rbac.common.entity.po.User;
import com.rbac.common.entity.vo.RouterVO;
import com.rbac.common.entity.vo.UserVO;
import com.rbac.common.exception.ServiceException;
import com.rbac.common.util.LoginResult;
import com.rbac.common.util.Result;
import com.rbac.common.util.ResultCode;
import com.rbac.feign.permiss.PermissionClient;
import com.rbac.sysuser.service.SysUserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @program: SysUserController
 * @description 系统用户控制层，用于管理用户的登录、登出、获取用户信息的功能
 * @author: 赵佶鑫
 * @create: 2024-08-23 19:07
 **/
@Slf4j
@RestController
@RequestMapping("/api/sysUser")
public class SysUserController {
    /**
     * Token字段
     */
    private static final String TOKEN = "token";
    /**
     * UserId字段
     */
    private static final String USERID = "userId";

    /**
     * sysUser服务层接口
     * @param
     * @return
     */
    @Resource
    private SysUserService sysUserService;


    /**
     * permission客户端，用于向sysUser远程提供服务
     * @param
     * @return
     */
    @Resource
    private PermissionClient permissionClient;



    /**
     * 获取用户信息的方法
     * @param request
     * @return Result<TokenVO>
     */
    @GetMapping("/getInfo")
    public Result getInfo(HttpServletRequest  request) {
        log.info("获取userInfo");
        String userId = request.getHeader(USERID);
        //校验参数是否为空
        if(userId == null || userId.isEmpty()) {
            throw new ServiceException("传入参数异常，请重新登陆");
        }
        //通过服务层获取用户信息
        UserDTO userDTO = sysUserService.queryUserInfo(Long.valueOf(userId));
        UserVO userVO = BeanUtil.copyProperties(userDTO, UserVO.class);
        // 返回数据
        log.info("用户信息查询成功");
        return Result.ok(userVO).message("用户信息查询成功");
    }

    /**
     * 用户登录方法，通过解析前端发送的请求头来获取用户信息并进行校验。
     * @param request
     * @param response
     * @return Result<TokenVO>
     */
    @PostMapping("/login")
    public LoginResult login(HttpServletRequest request, HttpServletResponse response) {
        log.info("用户登录");

        //从请求头中获取token和用户id
        String token = request.getHeader(TOKEN);
        String id = request.getHeader(USERID);

        //校验参数是否为空
        if(BeanUtil.isEmpty(token) || BeanUtil.isEmpty(id)) {
            throw new ServiceException("传入参数异常，请重新登陆");
        }
        Long userId = Long.valueOf(id);

        //通过服务层获取userId对应的用户信息
        UserDTO userDTO = sysUserService.queryUserInfo(userId);
        if(BeanUtil.isEmpty(userDTO)) {
            throw new ServiceException("业务层异常，无法获取用户信息");
        }
        LoginResult res = BeanUtil.copyProperties(userDTO, LoginResult.class);
        res.setToken(token);
        res.setCode(ResultCode.SUCCESS);
        res.setExpireTime(System.currentTimeMillis() + 1000 * 60 * 10L);
        if(BeanUtil.isEmpty(res)) {
            throw new ServiceException("业务层异常，类型转换异常");
        }
        return res;
    }


    /**
     * 用户登出方法，用户用于退出登录
     * @param request
     * @param response
     * @return Result<Object>
     */
    @PostMapping("/loginOut")
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        // 获取token
        String token = request.getParameter(TOKEN);
        // 如果没有从头部获取token，那么从参数里面获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getHeader(TOKEN);
            log.info(token);
        }

        return Result.ok().message("用户退出成功");
    }

    /**
     * 获取资源菜单方法，用于用户登录成功后获取自己权限对应的资源菜单
     * @param request
     * @return Result<List<RouterVO>>
     */
    @GetMapping("/getMenuList")
    public Result<List<RouterVO>> getMenuList(HttpServletRequest request) {
        // 开始匹配路由信息
        String id = request.getHeader(USERID);
        if(BeanUtil.isEmpty(id)) {
            throw new ServiceException("传入参数异常");
        }
//        makeRouterRequest
        return permissionClient.makeRouter(1L);
    }

    /**
     * 获取用户列表的所有用户
     * @param usernames
     * @return List<User>
     */
    @PostMapping("/getAllUserByUserNames")
    List<User> getAllUsers(@RequestBody List<String> usernames) {
        if( usernames == null) {
            throw new ServiceException("传入参数异常");
        }
        return sysUserService.getAll(usernames);
    }
}
