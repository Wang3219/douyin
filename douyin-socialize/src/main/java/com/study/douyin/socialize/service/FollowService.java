package com.study.douyin.socialize.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.socialize.entity.FollowEntity;

/**
 * 粉丝
 */
public interface FollowService extends IService<FollowEntity> {

    boolean action(String token, Integer toUserId, Integer actionType);

}
