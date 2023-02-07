package com.study.douyin.basic.controller;

import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.service.VideoService;
import com.study.douyin.basic.vo.ActionVo;
import com.study.douyin.basic.vo.PublishVo;
import com.study.douyin.basic.vo.UserVo;
import com.study.douyin.basic.vo.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/publish")
public class PublishController {

    @Value("${video-config.server-path}")
    private String SERVER_PATH;

    @Value("${video-config.video-save-path}")
    private String VIDEO_PATH;
    @Value("${video-config.frame-num}")
    private int FRAME_NUM;
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
            PublishVo success = PublishVo.success();

            // 获取所有需要返回的视频以及视频作者信息
            Video[] videoList = videoService.listVideoList(user);

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
        Video[] videoList = videoService.getVideoListByVideoIds(videoIds, token);
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
        //保存视频进本地
        ServletContext servletContext = session.getServletContext();
        String videosFolderPath = servletContext.getRealPath(VIDEO_PATH);//获取视频文件夹路径
        File videosFolder=new File(videosFolderPath);
        if (!videosFolder.exists()) {//如果不存在该文件夹，则创建
            videosFolder.mkdir();
        }
        //String videoTargetPath=staticPath+VIDEO_PATH+ File.separator+filename+DEFAULT_VIDEO_FORMAT;
        String videoTargetPath=videosFolderPath + File.separator+filename+DEFAULT_VIDEO_FORMAT;
        videoService.fetchVideoToFile(videoTargetPath, data);

        //保存封面进本地
        String coversFolderPath = servletContext.getRealPath(VIDEO_COVER_PATH);//获取封面文件夹路径
        File coversFolder=new File(coversFolderPath);
        if (!coversFolder.exists()) {//如果不存在该文件夹则创建
            coversFolder.mkdir();
        }
        //String coverTargetPath=staticPath+VIDEO_COVER_PATH+File.separator+filename+DEFAULT_IMG_FORMAT;
        String coverTargetPath=coversFolderPath+File.separator+filename+DEFAULT_IMG_FORMAT;
        videoService.fetchFrameToFile(videoTargetPath, coverTargetPath, FRAME_NUM);

        //向mysql中存入视频数据
        String videoPath=SERVER_PATH+VIDEO_PATH+File.separator+filename+DEFAULT_VIDEO_FORMAT;
        String coverPath=SERVER_PATH+VIDEO_COVER_PATH+File.separator+filename+DEFAULT_IMG_FORMAT;
        //coverPath="https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF";
        videoService.saveVideoMsg(userId, videoPath, coverPath, title);

        return ActionVo.success();
   }
}
