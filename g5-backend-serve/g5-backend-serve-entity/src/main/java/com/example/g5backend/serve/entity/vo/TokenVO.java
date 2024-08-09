package com.example.g5backend.serve.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: rbac
 * @description 向前端返回的Token类
 * @author: 寒旅
 * @create: 2022-06-12 09:26
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * token
     */
    private String token;

}
