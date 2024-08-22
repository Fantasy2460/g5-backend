package com.rbac.feign.sysuser;


import com.rbac.common.entity.po.User;
import com.rbac.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * user客户端
 *      暴露对外接口
 * @author Lzzh
 */
@FeignClient(name = "rbac-sysuser", url = "http://localhost:8002/api/sysUser")
public interface SysUserClient {
    /**
     * 获取用户信息
     * @param usernames 用户名
     * @return  用户信息
     */
    @PostMapping("/getAllUserByUserNames")
    List<User> getAllUsers(@RequestBody Set<String> usernames);
}
