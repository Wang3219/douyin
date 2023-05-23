package com.study.douyin.basic.service.impl;

import com.aliyun.oss.OSS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.douyin.basic.dao.VideoDao;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.entity.VideoEntity;
import com.study.douyin.basic.feign.InteractFeignService;
import com.study.douyin.basic.feign.SocializeFeignService;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.service.VideoService;
import com.study.douyin.basic.util.ThreadPool;
import com.study.douyin.basic.vo.User;
import com.study.douyin.basic.vo.Video;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service("videoService")
public class VideoServiceImpl extends ServiceImpl<VideoDao, VideoEntity> implements VideoService {

    @Autowired
    private SocializeFeignService socializeFeignService;

    @Autowired
    private UserService userService;

    @Autowired
    private InteractFeignService interactFeignService;

    @Autowired
    private OSS oss;

    @Value("${spring.cloud.alicloud.oss.bucket-name}")
    private String bucketName;

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
    @Cacheable(value = "video", key = "#user.userId", sync = true)
    @Override
    public Video[] listVideoList(UserEntity user) throws Exception {
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
        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int finalI = i;
            // 放入线程池中运行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                videoList[finalI] = new Video();
                videoList[finalI].setId(videos.get(finalI).getVideoId());
                videoList[finalI].setAuthor(author);
                videoList[finalI].setPlayurl(videos.get(finalI).getPlayUrl());
                videoList[finalI].setCoverurl(videos.get(finalI).getCoverUrl());

                videoList[finalI].setFavoriteCount(interactFeignService.favoriteCount(videos.get(finalI).getVideoId()));
                videoList[finalI].setCommentCount(interactFeignService.CommentCount(videos.get(finalI).getVideoId()));
                videoList[finalI].setFavorite(interactFeignService.isFavorite(user.getUserId(), videos.get(finalI).getVideoId()));

                videoList[finalI].setTitle(videos.get(finalI).getTitle());
            }, ThreadPool.executor);
            futureList.add(future);
        }
        // 阻塞主线程等待，避免主线程提前返回结果，导致数据错误
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        return videoList;
    }

    @Override
    public Video[] getVideoListByVideoIds(List<Integer> videoIds, int id) throws Exception {
        Video[] videoList = new Video[videoIds.size()];
        List<CompletableFuture> futureList = new ArrayList<>();
        for (int i=0; i < videoIds.size(); i++) {
            int finalI = i;
            // 放入线程池中运行
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                // 通过videoId查询视频基础信息
                int videoId = videoIds.get(finalI);
                VideoEntity videoEntity = this.getById(videoId);

                // 查询视频作者信息
                int userId = videoEntity.getUserId();
                UserEntity user = userService.getById(userId);

                // 封装数据
                User author = new User();
                author.setId(userId);
                author.setName(user.getUsername());
                author.setFollowCount(user.getFollowCount());
                author.setFollowerCount(user.getFollowerCount());
                author.setFollow(socializeFeignService.isFollow(userId, id));

                Video video = new Video();
                video.setAuthor(author);
                video.setId(videoId);
                video.setTitle(videoEntity.getTitle());
                video.setCoverurl(videoEntity.getCoverUrl());
                video.setPlayurl(videoEntity.getPlayUrl());
                video.setFavoriteCount(interactFeignService.favoriteCount(videoId));
                video.setFavorite(interactFeignService.isFavorite(id, videoId));
                video.setCommentCount(interactFeignService.CommentCount(videoId));

                videoList[finalI] = video;
            }, ThreadPool.executor);
            futureList.add(future);
        }

        // 阻塞主线程等待，避免主线程提前返回结果，导致数据错误
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
        return videoList;
    }

    @Override
    public String fetchVideoToFile(String videoTargetFile, MultipartFile data) throws IOException {
        // 将视频保存到oss
        oss.putObject(bucketName, videoTargetFile, new ByteArrayInputStream(data.getBytes()));
        Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
        String url = oss.generatePresignedUrl(bucketName, videoTargetFile, date).toString();
        log.info("视频保存成功："+videoTargetFile);
        return url;
    }

    @Override
    public String fetchFrameToFile(String videoFile, String targetFile) {
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
            ff.start();
            int length = ff.getLengthInFrames();
//            //*第几帧判断设置*//*
//            if (frameNum < 0) {
//                frameNum = 0;
//            }
//            if (frameNum > length) {
//                frameNum = length - 5;
//            }
            int i = 0;
            Frame f = null;
            while (i < length) {
                f = ff.grabFrame();
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定
                if ((i >= 5) && (f.image != null)) {
                    break;
                }
                i++;
            }
            //BufferedImage bi = f.image.getBufferedImage();
//            opencv_core.IplImage img = f.image;
//            int width = img.width();
//            int height = img.height();
//            BufferedImage bi;
//            if (width > height){
//                 bi = new BufferedImage(height, width, BufferedImage.TYPE_3BYTE_BGR);
//                //截取出来的图是歪的，旋转九十度
//                //BufferedImage targetImage = rotateClockwise90(f.image.getBufferedImage());
//                BufferedImage targetImage = f.image.getBufferedImage();
//                int coordinate = (width - height)/2;
//                bi.getGraphics().drawImage(targetImage.getScaledInstance(targetImage.getWidth(), targetImage.getHeight(), Image.SCALE_SMOOTH),
//                        0, coordinate , null);
//            } else {
//                bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//                BufferedImage targetImage = f.image.getBufferedImage();
//                bi.getGraphics().drawImage(targetImage.getScaledInstance(targetImage.getWidth(), targetImage.getHeight(), Image.SCALE_SMOOTH),
//                        0, 0 , null);
//            }
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bi = converter.getBufferedImage(f);

            // 将封面保存到oss
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bi, DEFAULT_IMG_FORMAT, os);
            oss.putObject(bucketName, targetFile, new ByteArrayInputStream(os.toByteArray()));
            Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7);
            String url = oss.generatePresignedUrl(bucketName, targetFile, date).toString();

            ff.stop();
            ff.close();
            log.info("视频封面保存成功："+url);
            return url;
        } catch (Exception e) {
            throw new RuntimeException("转换视频图片异常");
        }
    }

    // 失效模式，投稿发布更新数据库后删除“当前用户发布的视频列表”缓存数据
    @CacheEvict(value = "video", key = "#userId")
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
                //bufferedImage.setRGB(height - 1 - j, i , bi.getRGB(i, j));}}
                bufferedImage.setRGB(height - j - 1, i , bi.getRGB(i, j));}}
        return bufferedImage;
    }
}
