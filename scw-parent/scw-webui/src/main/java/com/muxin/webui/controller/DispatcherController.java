package com.muxin.webui.controller;

import com.muxin.common.response.AppResponse;
import com.muxin.webui.service.MemberServiceFeign;
import com.muxin.webui.service.ProjectServiceFeign;
import com.muxin.webui.vo.resp.ProjectVo;
import com.muxin.webui.vo.resp.UserRespVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public class DispatcherController {
    
    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/")
    public String toIndex(Model model){
        /*思路：首次从redis获取*/
        /*1、从redis获取项目信息列表*/
        List<ProjectVo> projectList = (List<ProjectVo>) redisTemplate.opsForValue().get("projectList");
        if(projectList==null){
            AppResponse<List<ProjectVo>> appResponse = projectServiceFeign.allProject();
            projectList = appResponse.getData();
            redisTemplate.opsForValue().set("projectStr",projectList,1, TimeUnit.HOURS);
        }
        model.addAttribute("projectList",projectList);
        return "index";
    }

    @RequestMapping(value = "/doLogin",method = RequestMethod.POST)
    public String doLogin(HttpSession session, String loginacct, String password){
        /*1、调用远程微服务*/
        AppResponse<UserRespVo> appResponse = memberServiceFeign.login(loginacct, password);
        UserRespVo userRespVo = appResponse.getData();

        log.info("登录账号:{},密码:{}",loginacct,password);
        log.info("登录用户数据:{}",userRespVo);

        if(userRespVo==null){//登录失败
            return "redirect:/login.html";
        }
        /*2、登录成功，将用户信息保存到session，session统一，通过redis实现*/
        session.setAttribute("sessionMember",userRespVo);
        /*3、获取前缀（用户登录前访问的url）*/
        String preUrl = (String) session.getAttribute("preUrl");
        /*4、页面跳转，存在前缀则跳前缀*/
        if(StringUtils.isEmpty(preUrl)){
            return "redirect:/";
        }
        return "redirect:/"+preUrl;
    }
}
