package com.study.douyin.basic.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.douyin.basic.entity.VideoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 视频表
 */
@Mapper
public interface VideoDao extends BaseMapper<VideoEntity> {
}
