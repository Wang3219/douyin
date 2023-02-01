package com.study.douyin.interact.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("douyin-basic")
public interface BasicFeignService {
    @GetMapping("/user/getUserIdByToken")
    Integer getUserIdByToken(@RequestParam("token") String token);
}
