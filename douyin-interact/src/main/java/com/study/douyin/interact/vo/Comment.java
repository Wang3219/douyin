package com.study.douyin.interact.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

@Data
public class Comment {
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论发布日期，格式 mm-dd
     */
    @Setter(onMethod_ = {@JsonProperty("create_date")})
    private String createDate;
    /**
     * 评论id
     */
    private long id;
    /**
     * 评论用户信息
     */
    private User user;
}
