package com.study.douyin.basic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.entity.VideoEntity;
import com.study.douyin.basic.vo.Video;

import java.util.List;

/**
 * 视频
 */
public interface VideoService extends IService<VideoEntity> {

    List<VideoEntity> searchVideosByUserId(int userId);

    Video[] listVideoList(UserEntity user);

}
