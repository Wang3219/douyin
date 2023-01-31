package com.study.douyin.basic.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户
 */
@Data
@TableName("user")
public class UserEntity {
    /**
     * 用户id
     */
    @TableId
    private Integer userId;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 粉丝总数
     */
    private Integer followCount;
    /**
     * 关注总数
     */
    private Integer followerCount;
    /**
     * 用户状态
     */
    private String statusCode;
    /**
     * 状态信息
     */
    private String statusMsg;
}
