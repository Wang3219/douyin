package com.study.douyin.interact.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.douyin.interact.util.StatusConstant;
import lombok.Data;
import lombok.Setter;

@Data
public class CommentVo {
    /**
     * 状态码，0-成功，其他值-失败
     */
    @Setter(onMethod_ = {@JsonProperty("status_code")})
    private long statusCode;
    /**
     * 返回状态描述
     */
    @Setter(onMethod_ = {@JsonProperty("status_msg")})
    private String statusMsg;
    /**
     * 评论成功返回评论内容，不需要重新拉取整个列表
     */
    private Comment comment;
    /**
     * 评论列表
     */
    @Setter(onMethod_ = {@JsonProperty("comment_list")})
    private Comment[] commentList;

    public static CommentVo success() {
        CommentVo commentVo = new CommentVo();
        commentVo.setStatusCode(StatusConstant.StatusEnum.SUCCESS.getCode());
        commentVo.setStatusMsg(StatusConstant.StatusEnum.SUCCESS.getMsg());
        return commentVo;
    }

    public static CommentVo fail() {
        CommentVo commentVo = new CommentVo();
        commentVo.setStatusCode(StatusConstant.StatusEnum.FAIL.getCode());
        commentVo.setStatusMsg(StatusConstant.StatusEnum.FAIL.getMsg());
        return commentVo;
    }
}
