package com.study.douyin.socialize.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.douyin.socialize.entity.FollowEntity;
import com.study.douyin.socialize.service.FollowService;
import com.study.douyin.socialize.vo.RelationVo;
import com.study.douyin.socialize.vo.User;
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

    /**
     * 获取关注列表
     * @param userId
     * @param token
     * @return
     */
    @GetMapping("/follow/list")
    public RelationVo getFollowList(@RequestParam("user_id") Integer userId, @RequestParam("token") String token) {
        User[] userList = followService.getFollowList(userId, token);
        if (userList == null)
            return RelationVo.fail();
        RelationVo success = RelationVo.success();
        success.setUserList(userList);
        return success;
    }

    /**
     * 获取粉丝列表
     * @param userId
     * @param token
     * @return
     */
    @GetMapping("/follower/list")
    public RelationVo getFollowerList(@RequestParam("user_id") Integer userId, @RequestParam("token") String token) {
        User[] userList = followService.getFollowerList(userId, token);
        if (userList == null)
            return RelationVo.fail();
        RelationVo success = RelationVo.success();
        success.setUserList(userList);
        return success;
    }

    /**
     * 获取好友列表
     * @param userId
     * @param token
     * @return
     */
    @GetMapping("/friend/list")
    public RelationVo getFriendList(@RequestParam("user_id") Integer userId, @RequestParam("token") String token) {
        User[] userList = followService.getFriendList(userId, token);
        if (userList == null)
            return RelationVo.fail();
        RelationVo success = RelationVo.success();
        success.setUserList(userList);
        return success;
    }
}
