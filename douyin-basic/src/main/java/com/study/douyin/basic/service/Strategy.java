package com.study.douyin.basic.service;

import com.study.douyin.basic.entity.VideoEntity;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 获取视频的策略
 * @Author: Xu1Aan
 * @Date: 2023/02/07/18:05
 * @Description:
 */
public interface Strategy {
    /**
     * 获取特定时间后上传的视频
     * @param timestamp
     * @return
     */
    public List<VideoEntity> getVideo(Timestamp timestamp);
}
