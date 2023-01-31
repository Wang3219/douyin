package com.study.douyin.basic.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 粉丝表
 */
@Data
@TableName("follow")
public class FollowEntity {
    /**
     * 主键
     */
    @TableId
    private Integer id;
    /**
     * 粉丝id
     */
    private Integer followId;
    /**
     * 用户id
     */
    private Integer userId;
}
