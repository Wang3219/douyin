package com.study.douyin.basic.service.impl;

import com.study.douyin.basic.entity.VideoEntity;
import com.study.douyin.basic.service.FeedService;
import com.study.douyin.basic.service.Strategy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 获得视频流
 * @Author: Xu1Aan
 * @Date: 2023/02/07/18:07
 * @Description:
 */
@Service("feedServiceImpl")
public class FeedServiceImpl implements FeedService {
    @Override
    public List<VideoEntity> getVideoByStrategy(Strategy strategy, Timestamp timestamp) {
        return strategy.getVideo(timestamp);
    }
}
