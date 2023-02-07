package com.study.douyin.basic.service;

import com.study.douyin.basic.entity.VideoEntity;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/02/07/18:03
 * @Description:
 */
public interface FeedService {
    /**
     * 从数据库中通过策略模式获取视频,timestamp指定视频截止事件,只选择在其后上传的视频
     */
    List<VideoEntity> getVideoByStrategy(Strategy strategy, Timestamp timestamp);
}
