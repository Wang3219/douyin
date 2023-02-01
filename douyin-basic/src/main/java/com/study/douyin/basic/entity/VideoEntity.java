package com.study.douyin.basic.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 视频表
 */
@Data
@TableName("video")
public class VideoEntity {
    /**
     * 视频id
     */
    @TableId
    private int videoId;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 视频播放地址
     */
    private String playUrl;
    /**
     * 视频封面地址
     */
    private String coverUrl;
    /**
     * 视频标题
     */
    private String title;
}
