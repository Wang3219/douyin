package com.study.douyin.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.basic.dao.UserDao;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.feign.SocializeFeignService;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private SocializeFeignService socializeFeignService;

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

        //初始化部分数据
        userEntity.setFollowCount(0);
        userEntity.setFollowerCount(0);

        baseMapper.insert(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity Login(String username, String password) {
        //查询当前用户名对应的用户
        UserEntity user = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", username));

        //数据库中加密的密码
        String passwordDB = user.getPassword();

        //判断用户输入的密码和数据库中密码是否匹配
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(password, passwordDB))
            //密码正确则返回用户
            return user;
        //密码错误返回null
        return null;
    }

    @Override
    public Integer getUserIdByToken(String token) {
        UserEntity user = this.getOne(new QueryWrapper<UserEntity>().eq("password", token));
        return user.getUserId();
    }

    @Override
    public User getUserByToken(String token) {
        UserEntity userEntity = this.getOne(new QueryWrapper<UserEntity>().eq("password", token));
        User user = new User();
        user.setId(userEntity.getUserId());
        user.setName(userEntity.getUsername());
        user.setFollowCount(userEntity.getFollowCount());
        user.setFollowerCount(userEntity.getFollowerCount());
        user.setFollow(socializeFeignService.isFollow((int) user.getId(), (int) user.getId()));
        return user;
    }

    @Override
    public User getUserById(int userId, int followId) {
        UserEntity userEntity = this.getById(userId);
        User user = new User();
        user.setId(userEntity.getUserId());
        user.setName(userEntity.getUsername());
        user.setFollowCount(userEntity.getFollowCount());
        user.setFollowerCount(userEntity.getFollowerCount());
        user.setFollow(socializeFeignService.isFollow(userId, followId));
        return user;
    }
}
