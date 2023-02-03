package com.study.douyin.interact.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.douyin.interact.entity.CommentEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao extends BaseMapper<CommentEntity> {
}
