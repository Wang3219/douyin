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
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;


@Slf4j
@Service("videoService")
public class VideoServiceImpl extends ServiceImpl<VideoDao, VideoEntity> implements VideoService {

    @Autowired
    private SocializeFeignService socializeFeignService;

    @Autowired
    private UserService userService;

    @Autowired
    private InteractFeignService interactFeignService;

    private static final String DEFAULT_IMG_FORMAT = "jpg";


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

    @Override
    public void fetchVideoToFile(String videoTargetFile, MultipartFile data) throws IOException {
        data.transferTo(new File(videoTargetFile));
        log.info("视频保存成功："+videoTargetFile);
    }

    @Override
    public void fetchFrameToFile(String videoFile, String targetFile, int frameNum) {
        try {
            File frameFile = new File(targetFile);
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
            ff.start();
            int length = ff.getLengthInFrames();
            //*第几帧判断设置*//*
            if (frameNum < 0) {
                frameNum = 0;
            }
            if (frameNum > length) {
                frameNum = length - 5;
            }
            //指定第几帧
            ff.setFrameNumber(frameNum);
            int i = 0;
            Frame f = null;
            while (i < length) {
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定
                f = ff.grabFrame();
                if ((i >= 5) && (f.image != null)) {
                    break;
                }
                i++;
            }
            opencv_core.IplImage img = f.image;
            int width = img.width();
            int height = img.height();
            BufferedImage bi = new BufferedImage(height, width, BufferedImage.TYPE_3BYTE_BGR);
            //截取出来的图是歪的，旋转九十度
            BufferedImage targetImage = rotateClockwise90(f.image.getBufferedImage());
            //BufferedImage targetImage = f.image.getBufferedImage();

            bi.getGraphics().drawImage(targetImage.getScaledInstance(targetImage.getWidth(), targetImage.getHeight(), Image.SCALE_SMOOTH),
                    0, 0, null);
            ff.flush();
            ff.stop();
            ImageIO.write(bi, DEFAULT_IMG_FORMAT, frameFile);
            log.info("视频封面保存成功："+targetFile);
        } catch (Exception e) {
            throw new RuntimeException("转换视频图片异常");
        }
    }



    @Override
    public void saveVideoMsg(int userId, String videoPath, String coverPath, String title) {
        VideoEntity video = new VideoEntity();
        video.setUserId(userId);
        video.setPlayUrl(videoPath);
        video.setCoverUrl(coverPath);
        video.setTitle(title);
        video.setPublishTime(new Timestamp(System.currentTimeMillis()));
        save(video);
    }

    /**
     * 将图片顺时针旋转90度（通过交换图像的整数像素RGB 值）
     * @param bi
     * @return
     */
    public static BufferedImage rotateClockwise90(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        BufferedImage bufferedImage = new BufferedImage(height, width, bi.getType());
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                //第一个参数为x轴，第二个为y轴
                bufferedImage.setRGB(height - 1 - j, i , bi.getRGB(i, j));}}
        return bufferedImage;
    }
}
