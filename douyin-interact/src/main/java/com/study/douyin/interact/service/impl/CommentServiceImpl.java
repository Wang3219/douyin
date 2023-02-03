package com.study.douyin.interact.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.interact.dao.CommentDao;
import com.study.douyin.interact.entity.CommentEntity;
import com.study.douyin.interact.service.CommentService;
import org.springframework.stereotype.Service;

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {
    @Override
    public Integer countByVideoId(Integer videoId) {
        int count = this.count(new QueryWrapper<CommentEntity>().eq("video_id", videoId));
        return count;
    }
}
