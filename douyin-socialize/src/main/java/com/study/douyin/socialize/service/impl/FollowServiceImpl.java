package com.study.douyin.socialize.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.socialize.dao.FollowDao;
import com.study.douyin.socialize.entity.FollowEntity;
import com.study.douyin.socialize.service.FollowService;
import org.springframework.stereotype.Service;

/**
 * 粉丝
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowDao, FollowEntity> implements FollowService {
}
