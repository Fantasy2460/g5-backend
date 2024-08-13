package com.example.g5backend.serve.service.security;


import com.example.g5backend.serve.entity.po.Permission;
import com.example.g5backend.serve.entity.po.User;
import com.example.g5backend.serve.service.PermissionService;
import com.example.g5backend.serve.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @name: CustomerUserDetailsServiceImpl
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 15:33
 **/
@Slf4j
@Component
public class CustomerUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUserName(username);
        if (user == null) {
            log.error("用户名或密码错误!");
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        List<Permission> permissionList = permissionService.findPermissionListByUserId(user.getId());
        List<String> collect = permissionList.stream()
                .filter(Objects::nonNull)
                .map(Permission::getCode).filter(Objects::nonNull)
                .collect(Collectors.toList());
        String[] strings = collect.toArray(new String[collect.size()]);
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
        user.setAuthorities(authorityList);
        user.setPermissionList(permissionList);
        return user;
    }
}
