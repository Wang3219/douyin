package com.study.douyin.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.douyin.basic.util.StatusConstant;
import lombok.Data;
import lombok.Setter;

@Data
public class UserInfoVo {
    /**
     * 状态码，0-成功，其他值-失败
     */
    @Setter(onMethod_ = {@JsonProperty("status_code")})
    private long statusCode;
    /**
     * 返回状态描述
     */
    @Setter(onMethod_ = {@JsonProperty("status_msg")})
    private String statusMsg;
    /**
     * 用户信息
     */
    private User user;

    //用户
    @Data
    public static class User {
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

    public static UserInfoVo success() {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setStatusCode(StatusConstant.StatusEnum.SUCCESS.getCode());
        userInfoVo.setStatusMsg(StatusConstant.StatusEnum.SUCCESS.getMsg());
        return userInfoVo;
    }

    public static UserInfoVo fail() {
        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setStatusCode(StatusConstant.StatusEnum.FAIL.getCode());
        userInfoVo.setStatusMsg(StatusConstant.StatusEnum.FAIL.getMsg());
        return userInfoVo;
    }
}


