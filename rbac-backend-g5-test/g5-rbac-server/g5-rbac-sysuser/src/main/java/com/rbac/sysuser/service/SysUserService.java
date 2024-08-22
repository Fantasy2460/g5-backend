package com.rbac.sysuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rbac.common.entity.dto.UserDTO;
import com.rbac.common.entity.po.User;
import com.rbac.common.entity.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @name: RoleService
 * @description
 * @author: 赵佶鑫
 * @create: 2024-08-07 15:21
 **/
@Service
public interface SysUserService extends IService<User> {

    /**
     * 添加方法
     *
     * @param userDTO
     * @return
     */
    Long save(UserDTO userDTO);


    /**
     * 更新用户
     *
     * @param userDTO
     * @return
     */
    boolean updateById(UserDTO userDTO);

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    User findUserByUserName(String username);

    /**
     * 根据用户id查询用户角色列表
     *
     * @param userId
     * @return
     */
    String[] findRolesByUserId(Long userId);

    /**
     * 删除用户信息
     *
     * @param userId
     * @return
     */
    boolean deleteUserById(Long userId);

    /**
     * 根据用户名称模糊查询用户
     *
     * @param username
     * @return
     */
    List<UserVO> findUsersByUserName(String username);


    /**
     * 分配角色
     *
     * @param userId
     * @param rolesId
     * @return
     */
    boolean saveUserRoles(Long userId, List<Long> rolesId);

    /**
     * 查询用户的基本信息
     * @param userId 用户id
     * @return userDTO
     */
    UserDTO queryUserInfo(Long userId);

    /**
     * 根据用户名称 获取到 用户id
     * @param usernames 用户名
     * @return 用户
     */
    List<User> getAll(List<String> usernames);
}
