package com.muxin.webui.controller;

import com.muxin.common.response.AppResponse;
import com.muxin.webui.service.OrderServiceFeign;
import com.muxin.webui.vo.resp.OrderInfoSubmitVo;
import com.muxin.webui.vo.resp.ReturnPayConfirmVo;
import com.muxin.webui.vo.resp.TOrder;
import com.muxin.webui.vo.resp.UserRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @RequestMapping("/save")
    public String orderPay(OrderInfoSubmitVo vo, HttpSession session){
        /*1、验证登录状态*/
        UserRespVo userRespVo = (UserRespVo) session.getAttribute("sessionMember");
        if(userRespVo==null){
            return "redirect:/login";
        }
        /*2、设置accessToken*/
        String accessToken = userRespVo.getAccessToken();
        vo.setAccessToken(accessToken);
        /*3、获取项目信息，returnConfirmSession*/
        ReturnPayConfirmVo confirmVo = (ReturnPayConfirmVo) session.getAttribute("returnConfirmSession");
        if(confirmVo==null){
            return "redirect:/login";
        }
        vo.setProjectid(confirmVo.getProjectId());
        vo.setReturnid(confirmVo.getId());
        vo.setRtncount(confirmVo.getNum());

        /*4、调用微服务生成订单*/
        AppResponse<TOrder> orderAppResponse = orderServiceFeign.saveOrder(vo);
        TOrder order = orderAppResponse.getData();

        //下单成功，打印相关信息待处理
        String orderName = confirmVo.getProjectName();
        System.out.println("orderNum:"+order.getOrdernum());
        System.out.println("money:"+order.getMoney());
        System.out.println("orderName:"+orderName);
        System.out.println("Remark:"+vo.getRemark());

        return "/member/minecrowdfunding";
    }
}
