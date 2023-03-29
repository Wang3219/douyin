# 极简版抖音 （开发中）
## 💡 项目简介

仓库: [https://github.com/wangwang3219/douyin](https://github.com/wangwang3219/douyin)

项目完成了抖音的简单后端，主要实现了功能有：用户的注册与登录，视频feed流，视频投稿，用户关注，聊天等操作

## 🚀 功能介绍

功能按照**系统（代码）架构**分类

- **基础模块**

  - 用户的注册与登录
  - 获取用户信息
  - 视频feed流
  - 视频投稿
  - 获取视频发布列表

- **互动模块**

  - 视频点赞
  - 视频评论
  - 获取视频喜欢列表
  - 获取视频评论列表

- **社交模块**

  - 关注用户
  - 获取关注列表
  - 获取粉丝列表
  - 获取好友列表
  - 向好友发送消息
  - 获取聊天记录


## 技术栈
  + SpringBoot
  + SpringCloud
  + Gateway
  + OpenFeign
  + Nacos
  + Mybatis-plus(mysql8.0)
  + SpringCache(Redis)
  + CompletableFuture 
  + OSS
  + kafka


## 待优化
  - Jwt
  - 抽取公共模块
  - ...

## 环境配置

**1. kafka**

- 参考文章：[Windows下 kafka安装及使用](https://blog.csdn.net/weixin_42838061/article/details/123953574?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-123953574-blog-126481448.235^v27^pc_relevant_recovery_v2&spm=1001.2101.3001.4242.1&utm_relevant_index=3)

**2. Redis**

- 参考文章：[Windows下Redis的安装和配置](https://blog.csdn.net/weixin_41381863/article/details/88231397?spm=1001.2101.3001.6661.1&utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-88231397-blog-126552221.235%5Ev27%5Epc_relevant_recovery_v2&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-1-88231397-blog-126552221.235%5Ev27%5Epc_relevant_recovery_v2&utm_relevant_index=1)

**3. Redis**

- 参考文章：[Windows下nacos单机模式部署](https://blog.csdn.net/KingCruel/article/details/127424002)


## 运行
  安装android/app-release.apk后双击右下角我的修改ip为服务器ip，端口为88。并修改application.yml中关于mysql、redis、nacos以及oss相关配置为自己账户密码端口后启动nacos和redis即可运行。
