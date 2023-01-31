package com.study.douyin.basic.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.study.douyin.basic.dto.UserDto;
import com.study.douyin.basic.entity.FollowEntity;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.service.FollowService;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.vo.UserInfoVo;
import com.study.douyin.basic.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户注册、登陆、信息获取
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @PostMapping("/register")
    public UserVo userRegister(@RequestParam("username") String username, @RequestParam("password") String password) {
        UserEntity user = userService.Register(username, password);

        // 已经注册过，注册失败
        if (user == null) {
            return UserVo.fail();
        }

        // 注册成功
        UserVo userVo = UserVo.success();
        userVo.setUserid(user.getUserId());
        userVo.setToken(user.getPassword());
        return userVo;
    }

    @PostMapping("/login")
    public UserVo userLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        UserEntity user = userService.Login(username, password);
        //登陆失败
        if (user == null)
            return UserVo.fail();

        //登陆成功
        UserVo userVo = UserVo.success();
        userVo.setUserid(user.getUserId());
        userVo.setToken(user.getPassword());
        return userVo;
    }

    @GetMapping("/")
    public UserInfoVo userInfo(@RequestParam("user_id") Integer userId, @RequestParam("token") String token) {
        //通过userId获得对应用户数据
        UserEntity user = userService.getOne(new QueryWrapper<UserEntity>().eq("password", token));

        if (user != null) {
            UserInfoVo success = UserInfoVo.success();
            success.setUser(new UserInfoVo.User());

            FollowEntity followEntity = followService.getOne(new QueryWrapper<FollowEntity>().eq("user_id", userId).eq("follow_id", user.getUserId()));
            success.getUser().setFollow(followEntity==null);
            success.getUser().setFollowCount(user.getFollowCount());
            success.getUser().setFollowerCount(user.getFollowerCount());
            success.getUser().setId(user.getUserId());
            success.getUser().setName(user.getUsername());

            return success;
        } else {
            return UserInfoVo.fail();
        }
    }

}
