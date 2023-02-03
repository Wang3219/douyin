package com.study.douyin.interact.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.interact.entity.FavoriteEntity;
import com.study.douyin.interact.vo.Video;

public interface FavoriteService extends IService<FavoriteEntity> {
    boolean action(String token, Integer videoId, Integer actionType);

    Video[] favoriteList(Integer userId, String token);

    Integer countByVideoId(Integer videoId);

    boolean isFavorite(Integer userId, Integer videoId);

}
