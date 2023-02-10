package com.study.douyin.socialize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.socialize.dao.FollowDao;
import com.study.douyin.socialize.entity.FollowEntity;
import com.study.douyin.socialize.feign.BasicFeignService;
import com.study.douyin.socialize.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 粉丝
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowDao, FollowEntity> implements FollowService {

    @Autowired
    private BasicFeignService basicFeignService;

    @Override
    public boolean action(String token, Integer toUserId, Integer actionType) {
        Integer userId = basicFeignService.getUserIdByToken(token);
        int count = 0;
        if (actionType == 1) {
            FollowEntity followEntity = new FollowEntity();
            followEntity.setUserId(toUserId);
            followEntity.setFollowId(userId);
            int num = baseMapper.selectCount(new QueryWrapper<FollowEntity>()
                    .eq("user_id", toUserId)
                    .eq("follow_id", userId));
            if (num == 0)
                count = baseMapper.insert(followEntity);
        } else if (actionType == 2) {
            count = baseMapper.delete(new QueryWrapper<FollowEntity>()
                    .eq("user_id", toUserId)
                    .eq("follow_id", userId));
        }
        return count == 1;
    }
}
