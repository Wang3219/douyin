package com.study.douyin.socialize.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("message")
public class MessageEntity {
    /**
     * 主键
     */
    @TableId
    private long id;
    /**
     * 发送时间
     */
    private Date messageTime;
    /**
     * 发送者id
     */
    private long fromUserId;
    /**
     * 接收者id
     */
    private long toUserId;
    /**
     * 消息内容
     */
    private String content;
}
