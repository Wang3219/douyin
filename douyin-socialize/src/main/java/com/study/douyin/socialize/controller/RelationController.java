package com.study.douyin.socialize.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.douyin.socialize.entity.FollowEntity;
import com.study.douyin.socialize.service.FollowService;
import com.study.douyin.socialize.vo.RelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 关注
     * @param token
     * @param toUserId 对方id
     * @param actionType 1-关注 2-取消关注
     * @return
     */
    @PostMapping("/action")
    public RelationVo action(@RequestParam("token") String token, @RequestParam("to_user_id") Integer toUserId, @RequestParam("action_type") Integer actionType) {
        boolean flag = followService.action(token, toUserId, actionType);
        if (flag)
            return RelationVo.success();
        return RelationVo.fail();
    }
}
