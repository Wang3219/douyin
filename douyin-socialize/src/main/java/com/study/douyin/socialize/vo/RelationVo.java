package com.study.douyin.socialize.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.douyin.socialize.util.StatusConstant;
import lombok.Data;
import lombok.Setter;

@Data
public class RelationVo {
    /**
     * 状态码，0-成功，其他值-失败
     */
    @Setter(onMethod_ = {@JsonProperty("status_code")})
    private int statusCode;
    /**
     * 返回状态描述
     */
    @Setter(onMethod_ = {@JsonProperty("status_msg")})
    private String statusMsg;
    /**
     * 用户信息列表
     */
    @Setter(onMethod_ = {@JsonProperty("user_list")})
    private User[] userList;

    public static RelationVo success() {
        RelationVo relationVo = new RelationVo();
        relationVo.setStatusCode(StatusConstant.StatusEnum.SUCCESS.getCode());
        relationVo.setStatusMsg(StatusConstant.StatusEnum.SUCCESS.getMsg());
        return relationVo;
    }

    public static RelationVo fail() {
        RelationVo relationVo = new RelationVo();
        relationVo.setStatusCode(StatusConstant.StatusEnum.FAIL.getCode());
        relationVo.setStatusMsg(StatusConstant.StatusEnum.FAIL.getMsg());
        return relationVo;
    }
}
