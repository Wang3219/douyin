package com.study.douyin.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.interact.dao.FavoriteDao;
import com.study.douyin.interact.entity.FavoriteEntity;
import com.study.douyin.interact.feign.BasicFeignService;
import com.study.douyin.interact.service.FavoriteService;
import com.study.douyin.interact.vo.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("favoriteService")
public class FavoriteServiceImpl extends ServiceImpl<FavoriteDao, FavoriteEntity> implements FavoriteService {

    @Autowired
    private BasicFeignService basicFeignService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @CacheEvict(value = "favorite", key = "#userId")
    @Override
    public boolean action(int userId, Integer videoId, Integer actionType) {
        // 当前token对应的user不存在
        if (userId == 0)
            return false;
        if (actionType == 1) {
            // 点赞
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
            if (entity == null){
                //count = baseMapper.insert(favoriteEntity);
                kafkaTemplate.send("favoriteQueue", JSON.toJSONString(favoriteEntity));
                return true;
            } else
                return false;
        } else if (actionType == 2) {
            // 取消点赞
            //int count = baseMapper.delete(new QueryWrapper<FavoriteEntity>().eq("user_id", userId).eq("video_id", videoId));
            kafkaTemplate.send("removeFavoriteQueue", JSON.toJSONString(videoId+":"+userId));
            return true;
        }
        // actionType的值不合法
        return false;
    }

    @Cacheable(value = "favorite", key = "#userId", sync = true)
    @Override
    public Video[] favoriteList(Integer userId, Integer id) {
        // 获取该用户所有喜欢的视频的videoId
        List<FavoriteEntity> favoriteEntities = this.list(new QueryWrapper<FavoriteEntity>().eq("user_id", userId));
        List<Integer> videoIds = favoriteEntities.stream().map(entity -> {
            return entity.getVideoId();
        }).collect(Collectors.toList());

        Video[] videoList = basicFeignService.videoList(videoIds, id);

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
