package com.study.douyin.interact.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.douyin.interact.entity.FavoriteEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteDao extends BaseMapper<FavoriteEntity> {
}
