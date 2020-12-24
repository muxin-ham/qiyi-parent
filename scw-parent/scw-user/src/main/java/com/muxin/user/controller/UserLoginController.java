package com.muxin.user.controller;

import com.muxin.common.response.AppResponse;
import com.muxin.user.component.SmsTemplate;
import com.muxin.user.po.TMember;
import com.muxin.user.service.UserService;
import com.muxin.user.vo.req.UserRegisterVo;
import com.muxin.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Api(tags = "用户登录/注册模块")
@Slf4j
public class UserLoginController {

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("发送验证码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "phoneNo",value = "手机号",required = true)
    })
    @PostMapping("/sendCode")
    public AppResponse sendCode(String phoneNo){
        //1、生成验证码
        String code = UUID.randomUUID().toString().substring(0, 4);
        //2、保存到redis中，用于之后的校验 redisTemplate.opsForValue().set(phoneNo,code);

        //3、发送验证码
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNo);
        querys.put("param", "code:"+ code);
        querys.put("tpl_id", "TP1711063");
        String sendCode = smsTemplate.sendCode(querys);
        if("".equals(sendCode)||"fail".equals(sendCode)){
            return AppResponse.fail("短信发送失败");
        }
        return AppResponse.success(sendCode);
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public AppResponse register(UserRegisterVo registerVo){
        /*1、校验验证码*/
        String code = redisTemplate.opsForValue().get(registerVo.getLoginacct());
        if(!StringUtils.isEmpty(code)){
            /*redis中有验证码*/
            boolean b = code.equalsIgnoreCase(registerVo.getCode());
            if(b){
                /*验证码正确,将vo转为业务层能用的数据对象*/
                TMember member = new TMember();
                BeanUtils.copyProperties(registerVo,member);
                /*将用户信息保存到数据库*/
                try {
                    userService.register(member);
                    log.debug("用户信息注册成功：{}",member.getLoginacct());
                    /*注册成功则删除验证码*/
                    redisTemplate.delete(registerVo.getLoginacct());
                    return AppResponse.success("注册成功");
                } catch (Exception e) {
                    log.debug("用户信息注册失败：{}",member.getLoginacct());
                    return AppResponse.fail(e.getMessage());
                }
            }else{
                return AppResponse.fail("验证码错误");
            }
        }else{
            return AppResponse.fail("验证码已过期，请重新获取");
        }
    }

    @ApiOperation("用户登录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username",value = "用户名",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true)
    })
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(String username,String password){
        /*1、查询用户信息,po*/
        TMember member = userService.login(username, password);
        if(member==null){
            /*登录失败*/
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户名或密码错误");
            return fail;
        }
        /*2、po转vo*/
        UserRespVo respVo = new UserRespVo();
        BeanUtils.copyProperties(member,respVo);
        /*3、设置token*/
        String token = UUID.randomUUID().toString().replace("-", "");
        respVo.setAccessToken(token);
        /*4、将token存入redis，并设置过期时间（存储用户登录状态,根剧令牌（key）得ID（value））*/
        redisTemplate.opsForValue().set(token,member.getId()+"",2,TimeUnit.HOURS);
        return AppResponse.success(respVo);
    }

}
