package com.rbac.common.exception;

/**
 * className: Message
 *
 * @author ZhaoJixin
 * @description: 管理全局异常信息
 * @date 2024/8/23 1:22
 * @version: 1.0
 */
public class Message {
    private static final String GLOBAL_ERR = "全局异常捕获!";
    public String Global(){
        return GLOBAL_ERR;
    }

    private static final String PERSISTEN_INSERT_ERR = "持久层插入异常";
    public String persistentInsert(){
        return PERSISTEN_INSERT_ERR;
    }

    private static final String PARAM_PASS_ERR = "服务层参数传入异常";
    public String paramPassErr(){
        return PARAM_PASS_ERR;
    }

    private static final String BUSINESS_PARAM_PASS_ERR   = "业务层参数传入异常";
    public String getBusinessParamPassErr(){
        return BUSINESS_PARAM_PASS_ERR;
    }
}
