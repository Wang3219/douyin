package com.study.douyin.interact.feign;

import com.study.douyin.interact.vo.User;
import com.study.douyin.interact.vo.Video;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("douyin-basic")
public interface BasicFeignService {
    @GetMapping("/user/getUserIdByToken")
    Integer getUserIdByToken(@RequestParam("token") String token);

    @GetMapping("/user/getUserByToken")
    User getUserByToken(@RequestParam("token") String token);

    @GetMapping("/user/getUserById")
    User getUserById(@RequestParam("userId") int userId, @RequestParam("followId") int followId);

    @GetMapping("/publish/videoList")
    Video[] videoList(@RequestParam("videoIds") List<Integer> videoIds, @RequestParam("token") String token);

}
