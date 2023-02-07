package com.study.douyin.interact.controller;

import com.study.douyin.interact.service.CommentService;
import com.study.douyin.interact.vo.Comment;
import com.study.douyin.interact.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/count")
    public int CommentCount(@RequestParam("videoId") Integer videoId) {
        return commentService.countByVideoId(videoId);
    }

    /**
     * 添加或删除评论
     * @param token
     * @param videoId
     * @param actionType
     * @param commentText
     * @param commentId
     * @return
     */
    @PostMapping("/action")
    public CommentVo PostComment(
            @RequestParam("token") String token,
            @RequestParam("video_id") int videoId,
            @RequestParam("action_type") int actionType,
            @RequestParam(value = "comment_text", required = false) String commentText,
            @RequestParam(value = "comment_id", required = false) Integer commentId) {
        Comment comment = commentService.PostComment(token, videoId, actionType, commentText, commentId);

        // 若返回id为1则表示出错
        if (comment.getId() == -1)
            return CommentVo.fail();

        CommentVo success = CommentVo.success();
        success.setComment(comment);
        return success;
    }

}
