package com.rbac.gateway.service;

import com.rbac.common.entity.po.User;
import com.rbac.feign.sysuser.SysUserClient;
import com.rbac.gateway.config.SystemSettingConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Lzzh
 * @version 1.0
 */
@Service
@Slf4j
public class SystemService {
    private static Set<Long> blackIds;
    private static Set<String> blackName;
    private static Set<Long> whiteIds;
    private static Set<String> whiteName;

    private static Boolean degradation;

    @Resource
    SysUserClient sysUserClient;

    @Autowired
    public SystemService(SystemSettingConfig config) {
        SystemService.blackName = new HashSet<>(config.getBlackList());
        SystemService.whiteName = new HashSet<>(config.getWhiteList());
        SystemService.degradation = config.getDegradation();
    }

    @PostConstruct
    void getUserId() {
        SystemService.blackIds = sysUserClient.getAllUsers(blackName)
               .stream()
               .map(User::getId).collect(Collectors.toSet());
        SystemService.whiteIds = sysUserClient.getAllUsers(whiteName)
               .stream()
               .map(User::getId).collect(Collectors.toSet());
    }

    /**
     * 检验用户是否为黑名单
     * @param userId userId
     * @return true
     */
    public static Boolean isBlackId(Long userId) {
        return blackIds.contains(userId);
    }

    /**
     * 检验用户是否为白名单
     * @param userId userId
     * @return  true
     */
    public static Boolean isWhiteId(Long userId) {
        return whiteIds.contains(userId);
    }

    /**
     * 系统降级
     * @return true 降级
     */
    public static  Boolean isDegradation() {
        return degradation;
    }

}
