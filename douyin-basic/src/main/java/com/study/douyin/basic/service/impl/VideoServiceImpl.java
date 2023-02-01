package com.study.douyin.basic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.basic.dao.VideoDao;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.entity.VideoEntity;
import com.study.douyin.basic.feign.SocializeFeignService;
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

            //TODO 远程调用互动接口，互动接口完成后再修改
            videoList[i].setFavoriteCount(0);
            videoList[i].setCommentCount(0);
            videoList[i].setFavorite(false);

            videoList[i].setTitle(videos.get(i).getTitle());
        }
        return videoList;
    }
}
