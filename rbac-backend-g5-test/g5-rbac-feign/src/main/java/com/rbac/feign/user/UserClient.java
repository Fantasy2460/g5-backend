package com.rbac.feign.user;

import com.rbac.common.entity.request.MakeRouterRequest;
import com.rbac.common.entity.vo.RouterVO;
import com.rbac.common.entity.vo.UserVO;
import com.rbac.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * user客户端
 *      暴露对外接口
 * @author Lzzh
 */
@FeignClient(name = "rbac-user", url = "http://localhost:8001")
public interface UserClient {
    /**
     * 根据username查询用户信息
     * @param username 用户名
     * @return
     */
    @GetMapping("/api/user/queryUser/{username}")
    Result<UserVO> queryUserByUsername(@PathVariable("username") @NotNull String username);

}
