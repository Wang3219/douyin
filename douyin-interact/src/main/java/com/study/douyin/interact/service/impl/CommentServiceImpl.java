package com.study.douyin.interact.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.interact.dao.CommentDao;
import com.study.douyin.interact.entity.CommentEntity;
import com.study.douyin.interact.feign.BasicFeignService;
import com.study.douyin.interact.service.CommentService;
import com.study.douyin.interact.util.ThreadPool;
import com.study.douyin.interact.vo.Comment;
import com.study.douyin.interact.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {

    @Autowired
    private BasicFeignService basicFeignService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

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
    @CacheEvict(value = "comment", key = "#videoId")
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
            // this.save(commentEntity);
            // 加入kafka消息队列 topic为commentQueue
            kafkaTemplate.send("commentQueue",JSON.toJSONString(commentEntity));
        } else if (actionType == 2) {
            commentEntity = this.getById(commentId);
            // 若评论存在且是当前用户发布的，才可以删除
            if (commentEntity != null && commentEntity.getUserId() == user.getId())
                //this.removeById(commentId);
                // 加入kafka消息队列 topic为 removeCommentQueue
                kafkaTemplate.send("removeCommentQueue",JSON.toJSONString(commentId));
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
    @Cacheable(value = "comment", key = "#videoId", sync = true)
    @Override
    public Comment[] getCommentList(String token, int videoId) throws Exception {
        User user = basicFeignService.getUserByToken(token);
        // 当前token不存在user
        if (user == null)
            return null;

        List<CommentEntity> comments = baseMapper.selectList(new QueryWrapper<CommentEntity>().eq("video_id", videoId));
        Comment[] commentList = new Comment[comments.size()];

        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            int finalI = i;
            // 放入线程池中运行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Comment comment = new Comment();
                CommentEntity commentEntity = comments.get(finalI);

                User author = basicFeignService.getUserById(commentEntity.getUserId(), (int) user.getId());

                comment.setId(commentEntity.getCommentId());
                comment.setContent(commentEntity.getCommentText());
                comment.setUser(author);
                comment.setCreateDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(commentEntity.getCreateDate()));

                commentList[finalI] = comment;
            }, ThreadPool.executor);
            futureList.add(future);
        }

        // 阻塞主线程等待，避免主线程提前返回结果，导致数据错误
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        return commentList;
    }
}
