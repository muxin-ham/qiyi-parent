package com.muxin.common.response;

import com.muxin.common.enums.ResponseCodeEnum;

public class AppResponse<T> {
    private Integer code;
    private String msg;
    private T data;

    /**
     * 快速响应成功
     * @param data
     * @return
     */
    public static<T> AppResponse<T> success(T data){
        AppResponse<T> resp = new AppResponse<T>();
        resp.setCode(ResponseCodeEnum.SUCCESS.getCode());
        resp.setMsg(ResponseCodeEnum.SUCCESS.getMsg());
        resp.setData(data);
        return resp;
    }

    /**
     * 快速响应失败
     */
    public static<T> AppResponse<T> fail(T data){
        AppResponse<T> resp = new AppResponse<T>();
        resp.setCode(ResponseCodeEnum.FAIL.getCode());
        resp.setMsg(ResponseCodeEnum.FAIL.getMsg());
        resp.setData(data);
        return resp;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
