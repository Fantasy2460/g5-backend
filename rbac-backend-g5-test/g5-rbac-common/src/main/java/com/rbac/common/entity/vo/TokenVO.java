package com.rbac.common.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: rbac
 * @description 向前端返回的Token类
 * @author: zzzhlee
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
