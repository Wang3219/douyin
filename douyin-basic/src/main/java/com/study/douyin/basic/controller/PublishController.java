package com.study.douyin.basic.controller;

import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.service.VideoService;
import com.study.douyin.basic.vo.ActionVo;
import com.study.douyin.basic.vo.PublishVo;
import com.study.douyin.basic.vo.UserVo;
import com.study.douyin.basic.vo.Video;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping("/publish")
public class PublishController {

    @Value("${video-config.video-save-path}")
    private String VIDEO_PATH;

    @Value("${video-config.video-cover-save-path}")
    private String VIDEO_COVER_PATH;

    private static final String DEFAULT_VIDEO_FORMAT = ".mp4";
    private static final String DEFAULT_IMG_FORMAT = ".jpg";

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public PublishVo list(@RequestParam("token") String token, @RequestParam("user_id") int userId) {

        // 查询当前用户信息
        UserEntity user = userService.getById(userId);

        // 如果用户存在则成功
        if (user != null && token.equals(user.getPassword())) {
            // 获取所有需要返回的视频以及视频作者信息
            Video[] videoList = new Video[0];
            try {
                videoList = videoService.listVideoList(user);
            } catch (Exception e) {
                e.printStackTrace();
                return PublishVo.fail();
            }
            // 成功
            PublishVo success = PublishVo.success();
            success.setVideoList(videoList);
            return success;
        }

        return PublishVo.fail();
    }

    /**
     * 通过videoId获取Video信息
     * @param videoIds
     * @return
     */
    @GetMapping("/videoList")
    public Video[] videoList(@RequestParam("videoIds") List<Integer> videoIds, @RequestParam("token") String token) {
        Video[] videoList = new Video[0];
        try {
            videoList = videoService.getVideoListByVideoIds(videoIds, token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return videoList;
    }

    @PostMapping("/action")
   public ActionVo action(@RequestParam("data") MultipartFile data, @RequestParam("token") String token,
                        @RequestParam("title") String title, HttpSession session) throws IOException {

        // 通过token获取用户id
        int userId = userService.getUserIdByToken(token);
        if (userId == 0){
            return ActionVo.fail();
        }
        // 生成视频名称
        String filename= UUID.randomUUID().toString();
        // 视频保存路径
        String videoTargetPath = VIDEO_PATH + "/" +filename+DEFAULT_VIDEO_FORMAT;
        String videoPath = videoService.fetchVideoToFile(videoTargetPath, data);

        //保存封面进本地
        // 封面路径
        String coverTargetPath=VIDEO_COVER_PATH + "/" + filename+DEFAULT_IMG_FORMAT;
        String coverPath = videoService.fetchFrameToFile(videoPath, coverTargetPath);

        //向mysql中存入视频数据
        videoService.saveVideoMsg(userId, videoPath, coverPath, title);

        return ActionVo.success();
   }
}
