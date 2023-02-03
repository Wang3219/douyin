package com.study.douyin.interact.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.interact.dao.FavoriteDao;
import com.study.douyin.interact.entity.FavoriteEntity;
import com.study.douyin.interact.feign.BasicFeignService;
import com.study.douyin.interact.service.FavoriteService;
import com.study.douyin.interact.vo.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("favoriteService")
public class FavoriteServiceImpl extends ServiceImpl<FavoriteDao, FavoriteEntity> implements FavoriteService {

    @Autowired
    private BasicFeignService basicFeignService;

    @Override
    public boolean action(String token, Integer videoId, Integer actionType) {
        if (actionType == 1) {
            // 点赞
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
            return count == 1;
        } else if (actionType == 2) {
            // 取消点赞
            Integer userId = basicFeignService.getUserIdByToken(token);

            int count = baseMapper.delete(new QueryWrapper<FavoriteEntity>().eq("user_id", userId).eq("video_id", videoId));
            return count == 1;
        }
        return false;
    }

    @Override
    public Video[] favoriteList(Integer userId, String token) {
        // 获取该用户所有喜欢的视频的videoId
        List<FavoriteEntity> favoriteEntities = this.list(new QueryWrapper<FavoriteEntity>().eq("user_id", userId));
        List<Integer> videoIds = favoriteEntities.stream().map(entity -> {
            return entity.getVideoId();
        }).collect(Collectors.toList());

        Video[] videoList = basicFeignService.videoList(videoIds, token);

        return videoList;
    }

    @Override
    public Integer countByVideoId(Integer videoId) {
        int count = this.count(new QueryWrapper<FavoriteEntity>().eq("video_id", videoId));
        return count;
    }

    @Override
    public boolean isFavorite(Integer userId, Integer videoId) {
        FavoriteEntity favoriteEntity = baseMapper.selectOne(new QueryWrapper<FavoriteEntity>()
                .eq("user_id", userId)
                .eq("video_id", videoId));

        return favoriteEntity != null;
    }
}
