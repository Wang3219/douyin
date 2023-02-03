package com.study.douyin.interact.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("comment")
public class CommentEntity {
    /**
     * 评论表id
     */
    @TableId
    private int commentId;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 视频id
     */
    private int videoId;
    /**
     * 评论文本
     */
    private String commentText;
    /**
     * 评论日期
     */
    private Date createDate;
}
