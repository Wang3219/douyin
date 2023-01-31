package com.study.douyin.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.basic.dto.UserDto;
import com.study.douyin.basic.entity.UserEntity;

/**
 * 用户
 */
public interface UserService extends IService<UserEntity> {
    UserEntity Register(String username, String password);

}
