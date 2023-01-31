package com.study.douyin.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DouyinBasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(DouyinBasicApplication.class, args);
    }

}
