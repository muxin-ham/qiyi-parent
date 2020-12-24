package com.muxin.webui.controller;

import com.muxin.common.response.AppResponse;
import com.muxin.webui.service.MemberServiceFeign;
import com.muxin.webui.service.ProjectServiceFeign;
import com.muxin.webui.vo.resp.ProjectDetailVo;
import com.muxin.webui.vo.resp.ReturnPayConfirmVo;
import com.muxin.webui.vo.resp.UserAddressVo;
import com.muxin.webui.vo.resp.UserRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @RequestMapping("/projectInfo")
    public String projectInfo(Integer id, Model model, HttpSession session){
        AppResponse<ProjectDetailVo> appResponse = projectServiceFeign.projectInfo(id);
        ProjectDetailVo detailVo = appResponse.getData();
        model.addAttribute("DetailVo",detailVo);
        /*放入session，共享详情，用于全局显示*/
        session.setAttribute("DetailVo",detailVo);
        return "project/project";
    }

    @RequestMapping("/confirm/returns/{projectId}/{returnId}")
    public String returnInfo(@PathVariable("projectId") Integer projectId,@PathVariable("returnId") Integer returnId, Model model, HttpSession session){
        /*1、从session中取出该项目*/
        ProjectDetailVo detailVo = (ProjectDetailVo) session.getAttribute("DetailVo");
        /*2、根据returnId获取回报信息*/
        AppResponse<ReturnPayConfirmVo> appResponse = projectServiceFeign.getTReturn(returnId);
        /*3、获取回报信息*/
        ReturnPayConfirmVo returnPayConfirmVo = appResponse.getData();
        returnPayConfirmVo.setNum(1);
        /*4、设置项目回报的项目id*/
        returnPayConfirmVo.setProjectId(projectId);
        /*5、设置项目回报的项目名称*/
        returnPayConfirmVo.setProjectName(detailVo.getName());
        /*6、获取项目发起人信息*/
        AppResponse<UserRespVo> memberResp = memberServiceFeign.findUser(detailVo.getMemberid());
        UserRespVo userRespVo = memberResp.getData();
        returnPayConfirmVo.setMemberId(detailVo.getMemberid());
        returnPayConfirmVo.setMemberName(userRespVo.getRealname());
        //添加项目回报信息到session
        session.setAttribute("returnConfirm",returnPayConfirmVo);
        //添加项目回报信息到Model
        model.addAttribute("returnConfirm",returnPayConfirmVo);
        return "project/pay-step-1";
    }

    @RequestMapping("/confirm/order/{num}")
    public String confirmOrder(@PathVariable("num")Integer num, Model model,HttpSession session){
        /*1、判断用户是否已经登录*/
        UserRespVo userRespVo = (UserRespVo) session.getAttribute("sessionMember");
        if(userRespVo==null){
            /*存取现在访问的页面，用于登陆后返回*/
            session.setAttribute("preUrl","project/confirm/order/"+num);
            return "redirect:/login.html";
        }
        /*2、获取用户token,微服务user通过token取id查取addressList*/
        String accessToken = userRespVo.getAccessToken();
        AppResponse<List<UserAddressVo>> listAppResponse = memberServiceFeign.addressList(accessToken);
        List<UserAddressVo> addressVoList = listAppResponse.getData();
        /*3、将地址存入request域中（model）*/
        model.addAttribute("addresses",addressVoList);
        /*4、从session中取出returnPayConfirmVo*/
        ReturnPayConfirmVo confirmVo = (ReturnPayConfirmVo) session.getAttribute("returnConfirm");
        confirmVo.setNum(num);
        confirmVo.setTotalPrice(new BigDecimal(num*confirmVo.getSupportmoney()+confirmVo.getFreight()));
        session.setAttribute("returnConfirmSession",confirmVo);
        return "project/pay-step-2";
    }
}
