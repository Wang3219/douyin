package com.study.douyin.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.douyin.basic.util.StatusConstant;
import lombok.Data;
import lombok.Setter;

@Data
public class PublishVo {
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
    private Video[] videoList;

    public static PublishVo success() {
        PublishVo publishVo = new PublishVo();
        publishVo.setStatusCode(StatusConstant.StatusEnum.SUCCESS.getCode());
        publishVo.setStatusMsg(StatusConstant.StatusEnum.SUCCESS.getMsg());
        return publishVo;
    }

    public static PublishVo fail() {
        PublishVo publishVo = new PublishVo();
        publishVo.setStatusCode(StatusConstant.StatusEnum.FAIL.getCode());
        publishVo.setStatusMsg(StatusConstant.StatusEnum.FAIL.getMsg());
        return publishVo;
    }
}
