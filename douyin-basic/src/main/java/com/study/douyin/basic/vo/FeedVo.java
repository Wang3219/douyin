package com.study.douyin.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.douyin.basic.util.StatusConstant;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/02/07/20:16
 * @Description:
 */
@Data
public class FeedVo {
    /**
     * 本次返回的视频中，发布最早的时间，作为下次请求时的latest_time
     */
    @Setter(onMethod_ = {@JsonProperty("next_time")})
    private Long nextTime;
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
     * 视频列表
     */
    @Setter(onMethod_ = {@JsonProperty("video_list")})
    private List<Video> videoList;

    public static FeedVo success() {
        FeedVo feedVo = new FeedVo();
        feedVo.setStatusCode(StatusConstant.StatusEnum.SUCCESS.getCode());
        feedVo.setStatusMsg(StatusConstant.StatusEnum.SUCCESS.getMsg());
        return feedVo;
    }

    public static FeedVo fail() {
        FeedVo feedVo = new FeedVo();
        feedVo.setStatusCode(StatusConstant.StatusEnum.FAIL.getCode());
        feedVo.setStatusMsg(StatusConstant.StatusEnum.FAIL.getMsg());
        return feedVo;
    }
}
