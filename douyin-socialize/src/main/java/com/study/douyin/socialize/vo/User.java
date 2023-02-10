package com.study.douyin.socialize.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

@Data
public class User {
    /**
     * 关注总数
     */
    @Setter(onMethod_ = {@JsonProperty("follow_count")})
    private long followCount;
    /**
     * 粉丝总数
     */
    @Setter(onMethod_ = {@JsonProperty("follower_count")})
    private long followerCount;
    /**
     * 用户id
     */
    private long id;
    /**
     * true-已关注，false-未关注
     */
    @Setter(onMethod_ = {@JsonProperty("is_follow")})
    private boolean isFollow;
    /**
     * 用户名称
     */
    private String name;
}
