package com.study.douyin.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.vo.User;

/**
 * 用户
 */
public interface UserService extends IService<UserEntity> {

    UserEntity Register(String username, String password);

    UserEntity Login(String username, String password);

    User getUserById(int userId, int followId);

}
