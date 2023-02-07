package com.study.douyin.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.douyin.basic.entity.VideoEntity;
import com.study.douyin.basic.service.Strategy;
import com.study.douyin.basic.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/02/07/18:05
 * @Description:
 */
@Service("strategy")
public class GetLatestStrategy implements Strategy {

    @Autowired
    VideoService videoService;

    @Value("${video-config.feed-video-max-num}")
    private String VIDEO_MAX_NUM;

    /**
     * 获取在指定时间后上传的视频，包含指定时间时刻
     * @param timestamp
     * @return
     */
    @Override
    public List<VideoEntity> getVideo(Timestamp timestamp) {
        QueryWrapper<VideoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("publish_time", timestamp).orderByDesc("publish_time")
                .last("limit "+VIDEO_MAX_NUM);
        return videoService.list(queryWrapper);
    }
}
