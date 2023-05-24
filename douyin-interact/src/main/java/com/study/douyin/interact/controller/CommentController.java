package com.study.douyin.interact.controller;

import com.study.douyin.interact.util.JwtUtils;
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
        if (!JwtUtils.verifyTokenOfUser(token))
            return CommentVo.fail();
        int userId = JwtUtils.getUserId(token);
        Comment comment = commentService.PostComment(userId, videoId, actionType, commentText, commentId);

        // 若返回null则表示出错
        if (comment == null)
            return CommentVo.fail();

        CommentVo success = CommentVo.success();
        success.setComment(comment);
        return success;
    }

    /**
     * 获取评论列表
     * @param token
     * @param videoId
     * @return
     */
    @GetMapping("/list")
    public CommentVo GetCommentList(@RequestParam("token") String token, @RequestParam("video_id") int videoId) {
        Comment[] commentList = new Comment[0];
        if (!JwtUtils.verifyTokenOfUser(token))
            return CommentVo.fail();
        int userId = JwtUtils.getUserId(token);
        try {
            commentList = commentService.getCommentList(userId, videoId);
        } catch (Exception e) {
            e.printStackTrace();
            return CommentVo.fail();
        }

        CommentVo success = CommentVo.success();
        success.setCommentList(commentList);
        return success;
    }

}
