package com.muxin.webui.service;

import com.muxin.common.response.AppResponse;
import com.muxin.webui.config.FeignConfig;
import com.muxin.webui.service.impl.MemberServiceFeignException;
import com.muxin.webui.vo.resp.UserAddressVo;
import com.muxin.webui.vo.resp.UserRespVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "SCW-USER",configuration = FeignConfig.class,fallback = MemberServiceFeignException.class)
public interface MemberServiceFeign {
    @PostMapping("/user/login")
    public AppResponse<UserRespVo> login(@RequestParam("username") String loginacct, @RequestParam("password") String password);

    @RequestMapping(value = "/user/findUser/{id}",method = RequestMethod.GET)
    public AppResponse<UserRespVo> findUser(@PathVariable("id") Integer id);

    @RequestMapping(value = "/user/info/address",method = RequestMethod.GET)
    public AppResponse<List<UserAddressVo>> addressList(@RequestParam("accessToken") String accessToken);
}
