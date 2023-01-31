package com.study.douyin.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.basic.dao.FollowDao;
import com.study.douyin.basic.entity.FollowEntity;
import com.study.douyin.basic.service.FollowService;
import org.springframework.stereotype.Service;

/**
 * 粉丝
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowDao, FollowEntity> implements FollowService {
}
