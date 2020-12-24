package com.muxin.webui.service.impl;

import com.muxin.common.response.AppResponse;
import com.muxin.webui.service.MemberServiceFeign;
import com.muxin.webui.vo.resp.UserAddressVo;
import com.muxin.webui.vo.resp.UserRespVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MemberServiceFeignException implements MemberServiceFeign {
    @Override
    public AppResponse login(String username, String password) {
        AppResponse<Object> response = AppResponse.fail(null);
        response.setMsg("调用远程服务器失败【登录】");
        return response;
    }

    @Override
    public AppResponse<UserRespVo> findUser(Integer id) {
        AppResponse<UserRespVo> fail = AppResponse.fail(null);
        fail.setMsg("调用远程服务器失败【获取用户信息】");
        return fail;
    }

    @Override
    public AppResponse<List<UserAddressVo>> addressList(String accessToken) {
        AppResponse<List<UserAddressVo>> fail = AppResponse.fail(null);
        fail.setMsg("调用远程服务器失败【查询地址列表】");
        return fail;
    }

}
