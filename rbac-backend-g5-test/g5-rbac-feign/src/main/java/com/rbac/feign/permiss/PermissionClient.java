package com.rbac.feign.permiss;

import com.rbac.common.entity.po.Permission;
import com.rbac.common.entity.request.MakeRouterRequest;
import com.rbac.common.entity.vo.RouterVO;
import com.rbac.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * permission客户端
 *      暴露对外接口
 * @author Lzzh
 */
@FeignClient(name = "rbac-permission", url = "http://localhost:8003/api/permission")
public interface PermissionClient {
    /**
     * 生成右侧路由菜单
     * @param userId permissionList 资源列表 pid 父资源
     * @return 路由菜单
     */
    @GetMapping("/makeRouter/{userId}")
    Result<List<RouterVO>> makeRouter(@PathVariable("userId") Long userId);

    /**
     * 获取全部子资源
     * @param id 父资源id
     * @return 子资源列表
     */
    @GetMapping("/getChildren/{id}")
    Set<Permission> getChildrenResource(@PathVariable("id") Long id);

//    /**
//     * 依据
//     * @param ids
//     * @return
//     */
//    Map<Long, List<RouterVO>> getRouterVOByRoleIds(Set<Long> ids);
}
