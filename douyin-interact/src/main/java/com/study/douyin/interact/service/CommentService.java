package com.study.douyin.interact.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.douyin.interact.entity.CommentEntity;
import com.study.douyin.interact.vo.Comment;

public interface CommentService extends IService<CommentEntity> {
    Integer countByVideoId(Integer videoId);

    Comment PostComment(String token, int videoId, int actionType, String commentText, Integer commentId);

}
