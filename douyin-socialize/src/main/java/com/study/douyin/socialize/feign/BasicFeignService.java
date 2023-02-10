package com.study.douyin.socialize.feign;

import com.study.douyin.socialize.vo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("douyin-basic")
public interface BasicFeignService {
    @GetMapping("/user/getUserIdByToken")
    Integer getUserIdByToken(@RequestParam("token") String token);

}
