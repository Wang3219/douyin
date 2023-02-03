package com.study.douyin.interact.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.interact.entity.CommentEntity;

public interface CommentService extends IService<CommentEntity> {
    Integer countByVideoId(Integer videoId);

}
