package com.example.g5backend.serve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.g5backend.serve.dao.UserDao;
import com.example.g5backend.serve.entity.dto.UserDTO;
import com.example.g5backend.serve.entity.po.User;
import com.example.g5backend.serve.entity.vo.UserVO;
import com.example.g5backend.serve.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: rbac
 * @description
 * @author: 寒旅
 * @create: 2022-06-10 14:48
 **/
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public Long save(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        this.save(user);
        return user.getId();
    }

    @Override
    public boolean updateById(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return this.updateById(user);
    }

    @Override
    public User findUserByUserName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public String[] findRolesByUserId(Long userId) {
        return userDao.findRolesByUserId(userId);
    }

    @Override
    public boolean deleteUserById(Long userId) {
        baseMapper.deleteUserRole(userId);
        int delete = baseMapper.deleteById(userId);
        return delete > 0;
    }

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

    @Override
    public Long[] findRolesIdByUserId(Long userId) {
        return baseMapper.findRolesIdByUserId(userId);
    }

    @Override
    public boolean saveUserRoles(Long userId, List<Long> roleIds) {
        baseMapper.deleteUserRole(userId);
        baseMapper.saveUserRoles(userId, roleIds);
        return true;
    }
}
