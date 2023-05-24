package com.study.douyin.basic.controller;

import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.entity.VideoEntity;
import com.study.douyin.basic.feign.InteractFeignService;
import com.study.douyin.basic.feign.SocializeFeignService;
import com.study.douyin.basic.service.FeedService;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.service.impl.GetLatestStrategy;
import com.study.douyin.basic.util.JwtUtils;
import com.study.douyin.basic.util.ThreadPool;
import com.study.douyin.basic.vo.FeedVo;
import com.study.douyin.basic.vo.PublishVo;
import com.study.douyin.basic.vo.User;
import com.study.douyin.basic.vo.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/02/07/17:23
 * @Description:
 */
@RestController
@Transactional
@Slf4j
public class FeedController {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedService feedService;

    @Autowired
    private GetLatestStrategy feedStrategy;

    @Autowired
    private InteractFeignService favoriteService;

    @Autowired
    private SocializeFeignService socializeFeignService;

    /**
     *  抖音首页返回视频流
     * @param latest_time
     * @param token
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/feed")
    public FeedVo feed(@RequestParam("latest_time") String latest_time, @RequestParam("token") String token) throws InterruptedException {
        Long latestTime="0".equals(latest_time)?System.currentTimeMillis():Long.parseLong(latest_time);
        if (!JwtUtils.verifyTokenOfUser(token))
            return FeedVo.fail();
        int userId = JwtUtils.getUserId(token);
        Timestamp timestamp=new Timestamp(latestTime);
        List<VideoEntity> videoList = feedService.getVideoByStrategy(feedStrategy, timestamp);

        Video[] videos = new Video[videoList.size()];
        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i = 0; i < videoList.size(); i++){
            VideoEntity video = videoList.get(i);
            int finalI = i;
            // 放入线程池中运行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                Video videoBuilder = new Video();
                //查询作者信息
                User userBuilder = new User();
                UserEntity userMsg = userService.getById(video.getUserId());
                userBuilder.setId(userMsg.getUserId());
                userBuilder.setName(userMsg.getUsername());
                userBuilder.setFollowCount(userMsg.getFollowCount());
                userBuilder.setFollowerCount(userMsg.getFollowerCount());
                userBuilder.setFollow(socializeFeignService.isFollow(userId, video.getUserId()));
                //查询视频信息
                int favoriteCount = favoriteService.favoriteCount(video.getVideoId());
                boolean isFavorite = favoriteService.isFavorite(userId, video.getVideoId());
                long commentCount = favoriteService.CommentCount(video.getVideoId());
                log.info(favoriteCount + "-" + isFavorite + "-" + commentCount);
                videoBuilder.setId(video.getVideoId());
                videoBuilder.setAuthor(userBuilder);
                videoBuilder.setPlayurl(video.getPlayUrl());
                videoBuilder.setCoverurl(video.getCoverUrl());
                videoBuilder.setFavoriteCount(favoriteCount);//已完善
                videoBuilder.setCommentCount(commentCount);//已完善
                videoBuilder.setFavorite(isFavorite);
                videoBuilder.setTitle(video.getTitle());
                videos[finalI] = videoBuilder;
            }, ThreadPool.executor);
            futureList.add(future);
        }
        try {
            // 阻塞主线程等待，避免主线程提前返回结果，导致数据错误
            CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return FeedVo.fail();
        }

        FeedVo feedVo = new FeedVo();
        feedVo.setStatusCode(0);
        feedVo.setStatusMsg("拉取视频成功");
        feedVo.setVideoList(videos);
        if (videoList.size() > 0) {
            feedVo.setNextTime(videoList.get(videoList.size() - 1).getPublishTime().getTime());
        } else {
            //否则设置下次返回七天内的视频，此行仅方便开发测试有用
            feedVo.setNextTime(System.currentTimeMillis()-7*24*60*60*1000);
        }
        log.info("feed:拉取视频成功");
        log.info(feedVo.getVideoList()[0].getTitle());
        return feedVo;
    }
}
