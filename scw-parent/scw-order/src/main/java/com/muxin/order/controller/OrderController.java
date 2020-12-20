package com.muxin.order.controller;

import com.muxin.common.response.AppResponse;
import com.muxin.order.po.TOrder;
import com.muxin.order.service.OrderService;
import com.muxin.order.vo.req.OrderInfoSubmitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/order")
@Api(tags = "保存订单")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/createOrder")
    @ApiOperation("保存订单")
    public AppResponse<TOrder> saveOrder(@RequestBody OrderInfoSubmitVo vo){
        try {
            TOrder order = orderService.saveOrder(vo);
            return AppResponse.success(order);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }
    }
}
