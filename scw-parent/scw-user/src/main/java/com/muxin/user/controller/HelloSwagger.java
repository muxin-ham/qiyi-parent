package com.muxin.user.controller;

import com.muxin.user.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "swagger测试")
@RestController
public class HelloSwagger {
    @ApiOperation("测试方法hello说明")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name",value = "姓名",required = true),
            @ApiImplicitParam(name = "email",value = "邮箱")
    })
    @GetMapping("/hello")
    public String hello(String name){
        return "OK:"+name;
    }

    @ApiOperation("保存用户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "name",value = "姓名",required = true),
            @ApiImplicitParam(name = "email",value = "邮箱")
    })
    @PostMapping("/save")
    public User save(String name,String email){
        User user = new User();
        user.setId(1);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
