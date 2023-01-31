package com.study.douyin.socialize.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.douyin.socialize.entity.FollowEntity;
import com.study.douyin.socialize.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relation")
public class RelationController {

    @Autowired
    private FollowService followService;

    @GetMapping("/follow/isFollow")
    public Boolean isFollow(@RequestParam("userId") Integer userId, @RequestParam("followId") Integer followId) {
        FollowEntity followEntity = followService.getOne(new QueryWrapper<FollowEntity>().eq("user_id", userId).eq("follow_id", followId));
        return followEntity!= null;
    }
}
