package com.study.douyin.basic.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.douyin.basic.util.StatusConstant;
import lombok.Data;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/02/07/13:59
 * @Description:
 */
@Data
public class ActionVo {
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

    public static ActionVo success() {
        ActionVo actionVo = new ActionVo();
        actionVo.setStatusCode(StatusConstant.StatusEnum.SUCCESS.getCode());
        actionVo.setStatusMsg(StatusConstant.StatusEnum.SUCCESS.getMsg());
        return actionVo;
    }

    public static ActionVo fail() {
        ActionVo actionVo = new ActionVo();
        actionVo.setStatusCode(StatusConstant.StatusEnum.FAIL.getCode());
        actionVo.setStatusMsg(StatusConstant.StatusEnum.FAIL.getMsg());
        return actionVo;
    }
}
