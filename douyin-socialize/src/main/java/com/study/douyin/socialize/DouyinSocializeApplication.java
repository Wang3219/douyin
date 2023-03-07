package com.study.douyin.socialize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DouyinSocializeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DouyinSocializeApplication.class, args);
    }

}
