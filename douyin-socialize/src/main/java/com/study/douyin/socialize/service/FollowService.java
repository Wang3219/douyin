package com.study.douyin.socialize.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.socialize.entity.FollowEntity;
import com.study.douyin.socialize.vo.User;

/**
 * 粉丝
 */
public interface FollowService extends IService<FollowEntity> {

    boolean action(String token, Integer toUserId, Integer actionType);

    User[] getFollowList(Integer userId, String token);

    User[] getFollowerList(Integer userId, String token);

    User[] getFriendList(Integer userId, String token);

}
