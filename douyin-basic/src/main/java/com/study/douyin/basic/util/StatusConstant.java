package com.study.douyin.basic.util;

public class StatusConstant {
    /**
     * 用户登陆注册返回对象状态吗以及状态描述
     */
    public enum  StatusEnum{
        SUCCESS(0,"成功"),FAIL(1,"失败");
        private int code;
        private String msg;

        StatusEnum(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
