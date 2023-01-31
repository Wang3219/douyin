package com.study.douyin.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.douyin.basic.entity.FollowEntity;
import com.study.douyin.basic.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 粉丝
 */
@Mapper
public interface FollowDao extends BaseMapper<FollowEntity> {
}
