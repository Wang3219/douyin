package com.study.douyin.basic.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("douyin-interact")
public interface InteractFeignService {

    @GetMapping("/favorite/count")
    int favoriteCount(@RequestParam("videoId") Integer videoId);

    @GetMapping("/favorite/isFavorite")
    boolean isFavorite(@RequestParam("userId") Integer userId, @RequestParam("videoId") Integer videoId);

    @GetMapping("/comment/count")
    int CommentCount(@RequestParam("videoId") Integer videoId);
}
