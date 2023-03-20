package com.study.douyin.socialize.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

@Data
public class Message {
    /**
     * 消息id
     */
    private long id;
    /**
     * 消息发送时间 yyyy-MM-dd HH:MM:ss
     */
    @Setter(onMethod_ = {@JsonProperty("create_time")})
    private long createTime;
    /**
     * 消息发送者id
     */
    @Setter(onMethod_ = {@JsonProperty("from_user_id")})
    private long fromUserid;
    /**
     * 消息接收者id
     */
    @Setter(onMethod_ = {@JsonProperty("to_user_id")})
    private long toUserid;
    /**
     * 消息内容
     */
    private String content;
}
