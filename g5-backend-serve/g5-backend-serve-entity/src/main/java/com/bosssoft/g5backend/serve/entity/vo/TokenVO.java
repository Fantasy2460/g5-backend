package com.bosssoft.g5backend.serve.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name: TokenVO
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 11:02
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
