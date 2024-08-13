package com.bosssoft.g5backend.serve.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.g5backend.serve.entity.po.User;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @name: CommonMetaObjectHandler
 * @description
 * @author: 曹鹏翔
 * @create: 2024-08-08 14:21
 **/
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {
    /**
     * 新增
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User operator = (User) authentication.getPrincipal();
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        this.strictUpdateFill(metaObject, "operator", String.class, operator.getUsername());
    }
    /**
     * 修改
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User operator = (User) authentication.getPrincipal();
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        this.strictUpdateFill(metaObject, "operator", String.class, operator.getUsername());
    }
}
