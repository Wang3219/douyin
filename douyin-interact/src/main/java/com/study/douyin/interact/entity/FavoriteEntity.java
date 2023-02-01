package com.study.douyin.interact.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("favorite")
public class FavoriteEntity {
    /**
     * 主键
     */
    @TableId
    private int favoriteId;
    /**
     * 用户id
     */
    private int userId;
    /**
     * 视频id
     */
    private int videoId;
}
