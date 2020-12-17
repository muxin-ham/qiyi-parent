package com.muxin.common.enums;

public enum ResponseCodeEnum {
    SUCCESS(200,"操作成功"),
    FAIL(500,"服务器异常"),
    NOT_FOUND(404,"资源未找到"),
    NOT_AUTHED(403,"无权限，拒绝访问"),
    PARAM_INVAILD(400,"提交参数非法");
    private Integer code;
    private String msg;

    private ResponseCodeEnum() {
    }

    private ResponseCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
