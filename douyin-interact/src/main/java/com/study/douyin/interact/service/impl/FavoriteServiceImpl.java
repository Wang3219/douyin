package com.study.douyin.interact.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.interact.dao.FavoriteDao;
import com.study.douyin.interact.entity.FavoriteEntity;
import com.study.douyin.interact.feign.BasicFeignService;
import com.study.douyin.interact.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("favoriteService")
public class FavoriteServiceImpl extends ServiceImpl<FavoriteDao, FavoriteEntity> implements FavoriteService {

    @Autowired
    private BasicFeignService basicFeignService;

    @Override
    public Integer like(String token, Integer videoId) {
        Integer userId = basicFeignService.getUserIdByToken(token);

        // 创建要存储的数据对象并填充数据
        FavoriteEntity favoriteEntity = new FavoriteEntity();
        favoriteEntity.setUserId(userId);
        favoriteEntity.setVideoId(videoId);

        // 查询是否已经点赞
        FavoriteEntity entity = baseMapper.selectOne(new QueryWrapper<FavoriteEntity>()
                .eq("user_id", userId)
                .eq("video_id", videoId));

        int count = 0;
        // 没点过赞才插入数据
        if (entity == null)
            count = baseMapper.insert(favoriteEntity);
        return count;
    }

    @Override
    public Integer unlike(String token, Integer videoId) {
        Integer userId = basicFeignService.getUserIdByToken(token);

        int count = baseMapper.delete(new QueryWrapper<FavoriteEntity>().eq("user_id", userId).eq("video_id", videoId));
        return count;
    }
}