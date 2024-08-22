package com.rbac.sysuser.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rbac.common.entity.dto.UserDTO;
import com.rbac.common.entity.po.User;
import com.rbac.common.entity.vo.UserVO;
import com.rbac.common.exception.ServiceException;
import com.rbac.feign.permiss.PermissionClient;
import com.rbac.feign.role.RoleClient;
import com.rbac.sysuser.dao.UserDao;
import com.rbac.sysuser.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @name: SysUserServiceImpl
 * @description 系统用户服务层实现，实现系统用户接口所对应的各种业务逻辑
 * @author: 赵佶鑫
 * @create: 2024-08-07 15:44
 **/
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class SysUserServiceImpl extends ServiceImpl<UserDao, User> implements SysUserService {

    /**
     * 获取用户列表的所有用户
     */
    @Resource
    private UserDao userDao;

    /**
     * 角色客户端
     */
    @Resource
    private RoleClient roleClient;

    /**
     * 保存用户信息，对应更改个人信息功能
     * @param userDTO
     * @return Long
     */
    @Override
    public Long save(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        this.save(user);
        return user.getId();
    }

    /**
     * 通过用户id更新用户信息
     * @param userDTO
     * @return boolean
     */
    @Override
    public boolean updateById(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return this.updateById(user);
    }

    /**
     * 通过用户名查找用户实体
     * @param username
     * @return boolean
     */
    @Override
    public User findUserByUserName(String username) {
        return userDao.selectUsersByUsername(username);
    }

    /**
     * 通过用户名的id查找用户对应的所有角色
     * @param userId
     * @return String[]
     */
    @Override
    public String[] findRolesByUserId(Long userId) {
        return userDao.findRolesByUserId(userId);
    }

    /**
     * 通过用户的id删除对应的用户
     * @param userId
     * @return boolean
     */
    @Override
    public boolean deleteUserById(Long userId) {
        baseMapper.deleteUserRole(userId);
        int delete = baseMapper.deleteById(userId);
        return delete > 0;
    }

    /**
     * 通过用户的用户名查找对应的用户
     * @param username
     * @return List<UserVO>
     */
    @Override
    public List<UserVO> findUsersByUserName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        List<User> userList = baseMapper.selectList(queryWrapper);
        List<UserVO> userVOList = new ArrayList<>();
        for (User user : userList) {
            userVOList.add(user.transformPoToVo());
        }
        return userVOList;
    }


    /**
     * 更新用户所对应的角色列表
     * @param userId
     * @param roleIds
     * @return boolean
     */
    @Override
    public boolean saveUserRoles(Long userId, List<Long> roleIds) {
        baseMapper.deleteUserRole(userId);
        baseMapper.saveUserRoles(userId, roleIds);
        return true;
    }

    /**
     * 查询用户的所有信息
     * @param userId
     * @return UserDTO
     */
    @Override
    public UserDTO queryUserInfo(Long userId) {
        if(BeanUtil.isEmpty(userId)) {
            throw new ServiceException("业务层传入参数为null");
        }
        User user = this.getById(userId);
        if(user == null) {
            throw new ServiceException("数据库查询异常");
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        userDTO.setRoles(roleClient.queryRoleNameByUserId(userId).toArray(new String[0]));

        return userDTO;
    }

    /**
     * 查询所有的用户列表
     * @param usernames
     * @return List<User>
     */
    @Override
    public List<User> getAll(List<String> usernames) {
        if(usernames == null ) {
            throw new ServiceException("业务层接受方法异常");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.in("username", usernames);
        return baseMapper.selectList(wrapper);
    }
}
