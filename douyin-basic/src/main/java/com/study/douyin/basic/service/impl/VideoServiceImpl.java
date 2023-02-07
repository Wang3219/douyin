package com.study.douyin.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.basic.dao.VideoDao;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.entity.VideoEntity;
import com.study.douyin.basic.feign.InteractFeignService;
import com.study.douyin.basic.feign.SocializeFeignService;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.service.VideoService;
import com.study.douyin.basic.vo.User;
import com.study.douyin.basic.vo.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("videoService")
public class VideoServiceImpl extends ServiceImpl<VideoDao, VideoEntity> implements VideoService {

    @Autowired
    private SocializeFeignService socializeFeignService;

    @Autowired
    private UserService userService;

    @Autowired
    private InteractFeignService interactFeignService;

    /**
     * 查询当前用户发布的所有视频基础信息
     * @param userId
     * @return
     */
    @Override
    public List<VideoEntity> searchVideosByUserId(int userId) {
        return this.list(new QueryWrapper<VideoEntity>().eq("user_id", userId));
    }

    /**
     * 查询并整合当前用户发布的所有视频以及视频作者信息
     * @param user
     * @return
     */
    @Override
    public Video[] listVideoList(UserEntity user) {
        //查询视频表获取当前用户发的视频的信息
        List<VideoEntity> videos = this.searchVideosByUserId(user.getUserId());

        //填入作者信息
        User author = new User();
        author.setId(user.getUserId());
        author.setName(user.getUsername());
        author.setFollow(socializeFeignService.isFollow(user.getUserId(), user.getUserId()));
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

            videoList[i].setFavoriteCount(interactFeignService.favoriteCount(videos.get(i).getVideoId()));
            videoList[i].setCommentCount(interactFeignService.CommentCount(videos.get(i).getVideoId()));
            videoList[i].setFavorite(interactFeignService.isFavorite(user.getUserId(), videos.get(i).getVideoId()));

            videoList[i].setTitle(videos.get(i).getTitle());
        }
        return videoList;
    }

    @Override
    public Video[] getVideoListByVideoIds(List<Integer> videoIds, String token) {
        Video[] videoList = new Video[videoIds.size()];
        for (int i=0; i < videoIds.size(); i++) {
            // 通过videoId查询视频基础信息
            Integer videoId = videoIds.get(i);
            VideoEntity videoEntity = this.getById(videoId);

            // 查询视频作者信息
            int userId = videoEntity.getUserId();
            UserEntity user = userService.getById(userId);

            // 获取当前用户信息
            UserEntity u = userService.getOne(new QueryWrapper<UserEntity>().eq("password", token));

            // 封装数据
            User author = new User();
            author.setId(userId);
            author.setName(user.getUsername());
            author.setFollowCount(user.getFollowCount());
            author.setFollowerCount(user.getFollowerCount());
            author.setFollow(socializeFeignService.isFollow(userId, u.getUserId()));

            Video video = new Video();
            video.setAuthor(author);
            video.setId(videoId);
            video.setTitle(videoEntity.getTitle());
            video.setCoverurl(videoEntity.getCoverUrl());
            video.setPlayurl(videoEntity.getPlayUrl());
            video.setFavoriteCount(interactFeignService.favoriteCount(videoId));
            video.setFavorite(interactFeignService.isFavorite(u.getUserId(), videoId));
            video.setCommentCount(interactFeignService.CommentCount(videoId));

            videoList[i] = video;
        }
        return videoList;
    }
}
