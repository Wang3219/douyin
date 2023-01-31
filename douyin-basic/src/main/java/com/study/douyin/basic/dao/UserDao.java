package com.study.douyin.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.douyin.basic.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
