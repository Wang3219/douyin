package com.study.douyin.interact.controller;

import com.study.douyin.interact.service.FavoriteService;
import com.study.douyin.interact.vo.FavoriteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/action")
    public FavoriteVo action(@RequestParam("token") String token, @RequestParam("video_id") Integer videoId, @RequestParam("action_type") Integer actionType) {
        boolean flag = favoriteService.action(token, videoId, actionType);

        if (flag)
            return FavoriteVo.success();

        return FavoriteVo.fail();
    }
}
