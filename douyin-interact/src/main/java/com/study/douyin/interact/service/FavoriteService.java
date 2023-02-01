package com.study.douyin.interact.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.interact.entity.FavoriteEntity;

public interface FavoriteService extends IService<FavoriteEntity> {
    Integer like(String token, Integer videoId);

    Integer unlike(String token, Integer videoId);

}
