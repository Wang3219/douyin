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


## 待优化
  - kafka
  - Jwt
  - 抽取公共模块
  - ...


## 运行
  安装android/app-release.apk后双击右下角我的修改ip为服务器ip，端口为88。并修改application.yml中关于mysql、redis、nacos以及oss相关配置为自己账户密码端口后启动nacos和redis即可运行。
