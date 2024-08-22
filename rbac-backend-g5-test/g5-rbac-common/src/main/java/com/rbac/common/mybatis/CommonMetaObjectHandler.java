package com.rbac.common.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import com.rbac.common.entity.po.User;
import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: rbac
 * @description 自动填充类:在新增和更新时自动填充时间和操作者
 * @author: zzzhlee
 * @create: 2022-06-14 21:35
 **/
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {
    /**
     * 新增
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User operator = (User) authentication.getPrincipal();
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
//        this.strictUpdateFill(metaObject, "operator", String.class, operator.getUsername());
    }
    /**
     * 修改
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User operator = (User) authentication.getPrincipal();
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
//        this.strictUpdateFill(metaObject, "operator", String.class, operator.getUsername());
    }
}
