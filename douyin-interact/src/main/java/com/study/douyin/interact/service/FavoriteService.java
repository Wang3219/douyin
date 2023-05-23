package com.study.douyin.interact.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.interact.entity.FavoriteEntity;
import com.study.douyin.interact.vo.Video;

public interface FavoriteService extends IService<FavoriteEntity> {
    boolean action(int userId, Integer videoId, Integer actionType);

    Video[] favoriteList(Integer userId, Integer id);

    Integer countByVideoId(Integer videoId);

    boolean isFavorite(Integer userId, Integer videoId);

}
