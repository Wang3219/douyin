package com.study.douyin.interact.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.interact.dao.CommentDao;
import com.study.douyin.interact.entity.CommentEntity;
import com.study.douyin.interact.feign.BasicFeignService;
import com.study.douyin.interact.service.CommentService;
import com.study.douyin.interact.vo.Comment;
import com.study.douyin.interact.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {

    @Autowired
    private BasicFeignService basicFeignService;

    @Override
    public Integer countByVideoId(Integer videoId) {
        int count = this.count(new QueryWrapper<CommentEntity>().eq("video_id", videoId));
        return count;
    }

    /**
     * 添加或删除评论
     * @param token
     * @param videoId
     * @param actionType
     * @param commentText
     * @param commentId
     * @return
     */
    @Override
    public Comment PostComment(String token, int videoId, int actionType, String commentText, Integer commentId) {
        // 获取当前用户信息
        User user = basicFeignService.getUserByToken(token);
        CommentEntity commentEntity = new CommentEntity();

        // actionType==1添加评论，否则删除评论
        if (actionType == 1) {
            commentEntity.setUserId((int) user.getId());
            commentEntity.setVideoId(videoId);
            commentEntity.setCommentText(commentText);
            commentEntity.setCreateDate(new Date());
            this.save(commentEntity);
        } else {
            commentEntity = this.getById(commentId);
            // 若评论存在且是当前用户发布的，才可以删除
            if (commentEntity != null && commentEntity.getUserId() == user.getId())
                this.removeById(commentId);
            // 若当前评论不存在则删除出错
            else {
                Comment comment = new Comment();
                comment.setId(-1);
                return comment;
            }
        }

        Comment comment = new Comment();
        comment.setId(commentEntity.getCommentId());
        comment.setUser(user);
        comment.setContent(commentEntity.getCommentText());
        comment.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(commentEntity.getCreateDate()));
        return comment;
    }
}
