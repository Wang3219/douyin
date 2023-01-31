package com.study.douyin.socialize.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.douyin.socialize.entity.FollowEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 粉丝
 */
@Mapper
public interface FollowDao extends BaseMapper<FollowEntity> {
}
