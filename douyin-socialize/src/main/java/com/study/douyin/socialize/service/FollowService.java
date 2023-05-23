package com.study.douyin.socialize.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.socialize.entity.FollowEntity;
import com.study.douyin.socialize.vo.User;

/**
 * 粉丝
 */
public interface FollowService extends IService<FollowEntity> {

    boolean action(int userId, Integer toUserId, Integer actionType);

    User[] getFollowList(Integer userId, Integer id);

    User[] getFollowerList(Integer userId, Integer id);

    User[] getFriendList(Integer userId, Integer id);

}
