package com.study.douyin.socialize.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.douyin.socialize.entity.MessageEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageDao extends BaseMapper<MessageEntity> {
}
