package com.study.douyin.basic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.entity.VideoEntity;
import com.study.douyin.basic.feign.SocializeFeignService;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.service.VideoService;
import com.study.douyin.basic.vo.PublishVo;
import com.study.douyin.basic.vo.User;
import com.study.douyin.basic.vo.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/publish")
public class PublishController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private SocializeFeignService socializeFeignService;

    @GetMapping("/list")
    public PublishVo list(@RequestParam("token") String token, @RequestParam("user_id") int userId) {
        UserEntity user = userService.getById(userId);
        if (user != null && token.equals(user.getPassword())) {
            PublishVo success = PublishVo.success();
            //查询视频表获取当前用户发的视频的信息
            List<VideoEntity> videos = videoService.list(new QueryWrapper<VideoEntity>().eq("user_id", userId));

            //填入作者信息
            User author = new User();
            author.setId(userId);
            author.setName(user.getUsername());
            author.setFollow(socializeFeignService.isFollow(userId, userId));
            author.setFollowCount(user.getFollowCount());
            author.setFollowerCount(user.getFollowerCount());

            //创建需要返回的video数组
            int size = videos.size();
            Video[] videoList = new Video[size];

            //查询并填入每个视频的数据
            for (int i = 0; i < size; i++) {
                videoList[i] = new Video();
                videoList[i].setId(videos.get(i).getVideoId());
                videoList[i].setAuthor(author);
                videoList[i].setPlayurl(videos.get(i).getPlayUrl());
                videoList[i].setCoverurl(videos.get(i).getCoverUrl());

                //TODO 远程调用互动接口，互动接口完成后再修改
                videoList[i].setFavoriteCount(0);
                videoList[i].setCommentCount(0);
                videoList[i].setFavorite(false);

                videoList[i].setTitle(videos.get(i).getTitle());
            }

            success.setVideoList(videoList);
            return success;
        }

        return PublishVo.fail();
    }
}
