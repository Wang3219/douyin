package com.study.douyin.basic.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("douyin-socialize")
public interface SocializeFeignService {
    @GetMapping("/relation/follow/isFollow")
    Boolean isFollow(@RequestParam("userId") Integer userId, @RequestParam("followId") Integer followId);
}
