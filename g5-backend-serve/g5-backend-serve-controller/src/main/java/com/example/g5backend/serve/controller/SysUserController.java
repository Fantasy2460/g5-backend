package com.example.g5backend.serve.controller;


import com.bosssoft.g5backend.serve.config.redis.RedisService;
import com.bosssoft.g5backend.serve.config.token.TokenService;
import com.example.g5backend.serve.entity.po.Permission;
import com.example.g5backend.serve.entity.po.User;
import com.example.g5backend.serve.entity.vo.RouterVO;
import com.example.g5backend.serve.entity.vo.TokenVO;
import com.example.g5backend.serve.entity.vo.UserVO;
import com.example.g5backend.serve.service.UserService;
import com.example.g5backend.serve.service.util.MenuTree;
import com.example.g5backend.serve.utils.JwtUtils;
import com.example.g5backend.serve.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: rbac
 * @description 系统用户控制器 用于控制用户登录、注销、刷新/生成token
 * @author: 寒旅
 * @create: 2022-06-12 09:29
 **/
@Slf4j
@RestController
@RequestMapping("/api/sysUser")
@CrossOrigin(origins = "http://localhost:9527")
public class SysUserController {

    private static final String TOKEN = "token";

    private static final String TOKEN_PREFIX = "token_";

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisService redisService;

    @Resource
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 刷新token
     * @param request
     * @return
     */
    @PostMapping("refreshToken")
    @CrossOrigin(origins = "http://localhost:9527")
    public Result<TokenVO> refreshToken(HttpServletRequest request) {
        // 从head中获取token信息
        String token = request.getHeader(TOKEN);
        // 判断head是否存在token
        if (ObjectUtils.isEmpty(token)) {
            // 从参数中获取token
            token = request.getParameter(TOKEN);
        }
        // 从Spring Security上下文中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 获取用户身份信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // 生成新token
        String newToken = "";
        // 严重token信息是否合法
        if (Boolean.TRUE.equals(jwtUtils.validateToken(token, userDetails))) {
            // 重新生成token
            newToken = jwtUtils.refreshToken(token);
        }
        // 获取本次token到期时间
        long expireTime = Jwts.parser()
                .setSigningKey(jwtUtils.getSecret())
                .parseClaimsJws(newToken.replace("jwt_", ""))
                .getBody().getExpiration().getTime();
        // 清除原来token信息
        String oldTokenKey = TOKEN_PREFIX + token;
        redisService.del(oldTokenKey);
        // 将新的token信息保存到缓存中
        String newTokenKey = TOKEN_PREFIX + newToken;
        redisService.set(newTokenKey, newToken, jwtUtils.getExpiration() / 1000);
        // 创建TokenVO对象
        TokenVO tokenVO = new TokenVO(expireTime, newToken);
        // 返回数据
        log.info("token刷新成功");
        return Result.ok(tokenVO).message("token刷新成功");
    }

    /**
     * 获取登录用户信息
     * @return
     */
    @GetMapping("/getInfo")
    @CrossOrigin(origins = "http://localhost:9527")
    public Result getInfo() {
        // 从Spring Security中获取用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 判断身份信息是否为空
        if (authentication == null) {
            log.error("用户信息查询失败");
            return Result.error().message("用户信息查询失败");
        }
        // 获取用户信息
        User user = (User) authentication.getPrincipal();
        // 获取角色权限编码字段
        String[] roles = userService.findRolesByUserId(user.getId());
        // 创建用户信息
        UserVO userVO = new UserVO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getIntroduction(),
                user.getPhone(),
                user.getEmail(),
                user.getAvatar(),
                roles);
        // 返回数据
        log.info("用户信息查询成功");
        return Result.ok(userVO).message("用户信息查询成功");
    }

    /**
     * 用户退出
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/loginOut")
    @CrossOrigin(origins = "http://localhost:9527")
    public Result<Object> logout(HttpServletRequest request, HttpServletResponse response) {
        // 获取token
        String token = request.getParameter(TOKEN);
        // 如果没有从头部获取token，那么从参数里面获取
        if (ObjectUtils.isEmpty(token)) {
            token = request.getHeader(TOKEN);
        }
        // 获取用户相关信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
        // 清空用户信息
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        // 清空redis里面的token
            String key = TOKEN_PREFIX + token;
            redisService.del(key);
        }
        log.info("用户退出成功");
        return Result.ok().message("用户退出成功");
    }

    /**
     * 获取菜单
     * @return
     */
    @GetMapping("/getMenuList")
    @CrossOrigin(origins = "http://localhost:9527")
    public Result<List<RouterVO>> getMenuList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Permission> permissionList = user.getPermissionList();
        List<Permission> collect = permissionList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<RouterVO> routerVOList = MenuTree.makeRouter(collect, 0L);
        return Result.ok(routerVOList).message("菜单获取成功");
    }

    /**
     * 生成接口幂等性token——ai 测试
     * @return
     */
    @GetMapping("/apiIdempotent")
    @CrossOrigin(origins = "http://localhost:9527")
    public Result apiIdempotent() {
        return tokenService.createToken();
    }
}
