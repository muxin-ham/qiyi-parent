package com.muxin.user.service;

import com.muxin.user.po.TMember;
import com.muxin.user.po.TMemberAddress;

import java.util.List;

public interface UserService {
    /*用户注册*/
    public void register(TMember member);

    /*用户登录*/
    public TMember login(String username,String password);

    /*根据用户id，获取用户信息*/
    public TMember findTMemberById(Integer id);

    /*获取用户地址*/
    public List<TMemberAddress> addressList(Integer memberId);
}
