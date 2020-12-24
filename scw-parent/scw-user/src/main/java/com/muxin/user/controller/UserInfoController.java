package com.muxin.user.controller;

import com.muxin.common.response.AppResponse;
import com.muxin.user.po.TMember;
import com.muxin.user.po.TMemberAddress;
import com.muxin.user.service.UserService;
import com.muxin.user.vo.resp.UserAddressVo;
import com.muxin.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "获取会员信息/更新个人信息/获取用户收货地址")
public class UserInfoController {
    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("获取用户地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accessToken",value = "访问令牌",required = true)
    })
    @RequestMapping(value = "/info/address",method = RequestMethod.GET)
    public AppResponse<List<UserAddressVo>> addressList(String accessToken){
        /*1、身份验证*/
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(memberId)){
            return AppResponse.fail(null);
        }
        /*2、获取地址列表*/
        List<TMemberAddress> addressList = userService.addressList(Integer.parseInt(memberId));
        /*3、po转vo*/
        List<UserAddressVo> addressListVo = new ArrayList<>();
        for (TMemberAddress address : addressList) {
            UserAddressVo userAddressVo = new UserAddressVo();
            BeanUtils.copyProperties(address,userAddressVo);
            addressListVo.add(userAddressVo);
        }
        return AppResponse.success(addressListVo);
    }

    @ApiOperation("根据id查询用户信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "ID",required = true),
    })
    @RequestMapping(value = "/findUser/{id}",method = RequestMethod.GET)
    public AppResponse<UserRespVo> findUser(@PathVariable Integer id){
        TMember tmember = userService.findTMemberById(id);
        UserRespVo userRespVo = new UserRespVo();
        BeanUtils.copyProperties(tmember,userRespVo);
        return AppResponse.success(userRespVo);
    }
}
