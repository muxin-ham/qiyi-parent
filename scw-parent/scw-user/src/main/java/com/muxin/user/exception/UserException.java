package com.muxin.user.exception;

import com.muxin.user.enums.UserExceptionEnum;
/**
 * 单表所有用户异常
 * 邮箱
 * 用户名
 * 密码
 */
public class UserException extends RuntimeException {
    public UserException(UserExceptionEnum exceptionEnum){
        super(exceptionEnum.getMsg());
    }
}
