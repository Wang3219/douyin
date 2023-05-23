package com.study.douyin.socialize.feign;

import com.study.douyin.socialize.vo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("douyin-basic")
public interface BasicFeignService {
    @GetMapping("/user/getUserById")
    User getUserById(@RequestParam("userId") int userId, @RequestParam("followId") int followId);

}
