package com.example.g5backend.serve.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: rbac
 * @description 根据Permission生成的前段路由结构
 * @author: 寒旅
 * @create: 2022-06-21 13:48
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 对应组件
     */
    private String component;

    /**
     * 是否显示
     */
    private Boolean alwaysShow;

    /**
     * 路由meta信息
     */
    private Meta meta;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta implements Serializable {

        /**
         * 标题
         */
        private String title;

        /**
         * 图标
         */
        private String icon;

        /**
         * 角色列表
         */
        private transient String[] roles;

    }

    /**
     * 子路由
     */
    private List<RouterVO> children;

}