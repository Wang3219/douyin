package com.study.douyin.interact.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

@Data
public class Video {
    /**
     * 视频作者信息
     */
    private User author;
    /**
     * 视频的评论总数
     */
    @Setter(onMethod_ = {@JsonProperty("comment_count")})
    private long commentCount;
    /**
     * 视频封面地址
     */
    @Setter(onMethod_ = {@JsonProperty("cover_url")})
    private String coverurl;
    /**
     * 视频的点赞总数
     */
    @Setter(onMethod_ = {@JsonProperty("favorite_count")})
    private long favoriteCount;
    /**
     * 视频唯一标识
     */
    private long id;
    /**
     * true-已点赞，false-未点赞
     */
    @Setter(onMethod_ = {@JsonProperty("is_favorite")})
    private boolean isFavorite;
    /**
     * 视频播放地址
     */
    @Setter(onMethod_ = {@JsonProperty("play_url")})
    private String playurl;
    /**
     * 视频标题
     */
    private String title;
}
