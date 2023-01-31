package com.study.douyin.basic.controller;


import com.study.douyin.basic.dto.UserDto;
import com.study.douyin.basic.entity.UserEntity;
import com.study.douyin.basic.service.UserService;
import com.study.douyin.basic.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户注册、登陆、信息获取
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserVo userRegister(@RequestParam("username") String username, @RequestParam("password") String password) {
        UserEntity newUser = userService.Register(username, password);

        // 已经注册过，注册失败
        if (newUser == null) {
            return UserVo.fail();
        }

        // 注册成功
        UserVo userVo = UserVo.success();
        userVo.setUserid(newUser.getUserId());
        userVo.setToken(newUser.getUsername() + newUser.getPassword());
        return userVo;
    }

}
