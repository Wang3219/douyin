package com.study.douyin.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.basic.dao.UserDao;
import com.study.douyin.basic.dto.UserDto;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {


    @Override
    public UserEntity Register(String username, String password) {
        UserEntity userEntity = new UserEntity();

        //判断当前用户是否存在
        UserEntity user = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", username));
        if (user != null)
            return null;

        //设置用户名
        userEntity.setUsername(username);

        //密码加密存储
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(passwordEncoder.encode(password));

        baseMapper.insert(userEntity);
        return userEntity;
    }
}
