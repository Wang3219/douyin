package com.study.douyin.interact.controller;

import com.study.douyin.interact.feign.BasicFeignService;
import com.study.douyin.interact.service.FavoriteService;
import com.study.douyin.interact.vo.FavoriteVo;
import com.study.douyin.interact.vo.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private BasicFeignService basicFeignService;

    /**
     * 点赞或取消点赞
     * @param token
     * @param videoId
     * @param actionType 1-点赞 2-取消点赞
     * @return
     */
    @PostMapping("/action")
    public FavoriteVo action(@RequestParam("token") String token, @RequestParam("video_id") Integer videoId, @RequestParam("action_type") Integer actionType) {
        int userId = basicFeignService.getUserIdByToken(token);
        boolean flag = favoriteService.action(userId, videoId, actionType);

        if (flag)
            return FavoriteVo.success();

        return FavoriteVo.fail();
    }

    /**
     * 获取喜欢列表
     * @param userId
     * @param token
     * @return
     */
    @GetMapping("/list")
    public FavoriteVo list(@RequestParam("user_id") Integer userId, @RequestParam("token") String token) {
        Video[] videolist = favoriteService.favoriteList(userId, token);
        FavoriteVo success = FavoriteVo.success();
        success.setVideoList(videolist);
        return success;
    }

    /**
     * 通过videoId查询有多少人喜欢该视频
     * @param videoId
     * @return
     */
    @GetMapping("/count")
    public int favoriteCount(@RequestParam("videoId") Integer videoId) {
        int count = favoriteService.countByVideoId(videoId);
        return count;
    }

    @GetMapping("/isFavorite")
    public boolean isFavorite(@RequestParam("userId") Integer userId, @RequestParam("videoId") Integer videoId) {
        return favoriteService.isFavorite(userId, videoId);
    }
}
