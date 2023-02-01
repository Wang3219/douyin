package com.study.douyin.interact.controller;

import com.study.douyin.interact.service.FavoriteService;
import com.study.douyin.interact.vo.FavoriteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/action")
    public FavoriteVo action(@RequestParam("token") String token, @RequestParam("video_id") Integer videoId, @RequestParam("action_type") Integer actionType) {
        int max = 0;
        if (actionType == 1) {
            // 点赞
            int count = favoriteService.like(token, videoId);
            max = Math.max(max, count);
        } else if (actionType == 2) {
            // 取消点赞
            int count = favoriteService.unlike(token, videoId);
            max = Math.max(max, count);
        }

        if (max == 0)
            return FavoriteVo.fail();

        return FavoriteVo.success();
    }
}
