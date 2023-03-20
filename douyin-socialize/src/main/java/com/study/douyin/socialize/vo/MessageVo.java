package com.study.douyin.socialize.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.douyin.socialize.util.StatusConstant;
import lombok.Data;
import lombok.Setter;

@Data
public class MessageVo {
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
     * 用户列表
     */
    @Setter(onMethod_ = {@JsonProperty("message_list")})
    private Message[] messageList;

    public static MessageVo success() {
        MessageVo messageVo = new MessageVo();
        messageVo.setStatusCode(StatusConstant.StatusEnum.SUCCESS.getCode());
        messageVo.setStatusMsg(StatusConstant.StatusEnum.SUCCESS.getMsg());
        return messageVo;
    }

    public static MessageVo fail() {
        MessageVo messageVo = new MessageVo();
        messageVo.setStatusCode(StatusConstant.StatusEnum.FAIL.getCode());
        messageVo.setStatusMsg(StatusConstant.StatusEnum.FAIL.getMsg());
        return messageVo;
    }
}
