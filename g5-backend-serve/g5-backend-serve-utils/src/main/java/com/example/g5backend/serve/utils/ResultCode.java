package com.example.g5backend.serve.utils;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-10 20:27
 **/
public class ResultCode {

    /**
     * 私有构造方法 防止其他类访问
     */
    private ResultCode(){}

    /**
     * 成功状态码
     */
    public static final Integer SUCCESS = 20000;

    /**
     * 失败状态码
     */
    public static final Integer ERROR = 500;

    /**
     * 未登录状态码
     */
    public static final int NO_LOGIN = 600;

    /**
     * 没有权限状态码
     */
    public static final int NO_AUTH = 700;

}
