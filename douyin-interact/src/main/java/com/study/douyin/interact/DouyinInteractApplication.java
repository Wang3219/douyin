package com.study.douyin.interact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DouyinInteractApplication {

    public static void main(String[] args) {
        SpringApplication.run(DouyinInteractApplication.class, args);
    }

}
