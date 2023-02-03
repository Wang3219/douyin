package com.study.douyin.interact.controller;

import com.study.douyin.interact.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/count")
    public int CommentCount(@RequestParam("videoId") Integer videoId) {
        return commentService.countByVideoId(videoId);
    }
}
