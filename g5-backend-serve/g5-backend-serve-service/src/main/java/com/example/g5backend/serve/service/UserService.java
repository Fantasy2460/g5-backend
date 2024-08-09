package com.example.g5backend.serve.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.example.g5backend.serve.entity.dto.UserDTO;
import com.example.g5backend.serve.entity.po.User;
import com.example.g5backend.serve.entity.vo.UserVO;

import java.util.List;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-10 14:48
 **/
public interface UserService extends IService<User> {

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
     * 根据用户id返回角色id列表
     *
     * @param userId
     * @return
     */
    Long[] findRolesIdByUserId(Long userId);

    /**
     * 分配角色
     *
     * @param userId
     * @param rolesId
     * @return
     */
    boolean saveUserRoles(Long userId, List<Long> rolesId);
}