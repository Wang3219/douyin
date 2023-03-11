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
import java.util.List;

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
        // 当前token不存在user
        if (user == null)
            return null;
        CommentEntity commentEntity = new CommentEntity();

        // actionType==1添加评论，否则删除评论
        if (actionType == 1) {
            commentEntity.setUserId((int) user.getId());
            commentEntity.setVideoId(videoId);
            commentEntity.setCommentText(commentText);
            commentEntity.setCreateDate(new Date());
            this.save(commentEntity);
        } else if (actionType == 2) {
            commentEntity = this.getById(commentId);
            // 若评论存在且是当前用户发布的，才可以删除
            if (commentEntity != null && commentEntity.getUserId() == user.getId())
                this.removeById(commentId);
            // 若当前评论不存在则删除出错
            else
                return null;
        }
        // actionType不为1或2
        else
            return null;

        Comment comment = new Comment();
        comment.setId(commentEntity.getCommentId());
        comment.setUser(user);
        comment.setContent(commentEntity.getCommentText());
        comment.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(commentEntity.getCreateDate()));
        return comment;
    }

    /**
     * 获取评论列表
     * @param token
     * @param videoId
     * @return
     */
    @Override
    public Comment[] getCommentList(String token, int videoId) {
        User user = basicFeignService.getUserByToken(token);
        // 当前token不存在user
        if (user == null)
            return null;

        List<CommentEntity> comments = baseMapper.selectList(new QueryWrapper<CommentEntity>().eq("video_id", videoId));
        Comment[] commentList = new Comment[comments.size()];
        for (int i = 0; i < comments.size(); i++) {
            Comment comment = new Comment();
            CommentEntity commentEntity = comments.get(i);

            User author = basicFeignService.getUserById(commentEntity.getUserId(), (int) user.getId());

            comment.setId(commentEntity.getCommentId());
            comment.setContent(commentEntity.getCommentText());
            comment.setUser(author);
            comment.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(commentEntity.getCreateDate()));

            commentList[i] = comment;
        }

        return commentList;
    }
}
