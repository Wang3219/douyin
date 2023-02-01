package com.study.douyin.basic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.basic.dao.VideoDao;
import com.study.douyin.basic.entity.VideoEntity;
import com.study.douyin.basic.service.VideoService;
import org.springframework.stereotype.Service;

@Service("videoService")
public class VideoServiceImpl extends ServiceImpl<VideoDao, VideoEntity> implements VideoService {
}
