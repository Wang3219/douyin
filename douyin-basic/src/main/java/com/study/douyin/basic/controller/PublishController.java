package com.study.douyin.basic.controller;

import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.service.VideoService;
import com.study.douyin.basic.vo.PublishVo;
import com.study.douyin.basic.vo.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publish")
public class PublishController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public PublishVo list(@RequestParam("token") String token, @RequestParam("user_id") int userId) {

        // 查询当前用户信息
        UserEntity user = userService.getById(userId);

        // 如果用户存在则成功
        if (user != null) {
            PublishVo success = PublishVo.success();

            // 获取所有需要返回的视频以及视频作者信息
            Video[] videoList = videoService.listVideoList(user);

            success.setVideoList(videoList);
            return success;
        }

        return PublishVo.fail();
    }
}
