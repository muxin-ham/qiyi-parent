package com.muxin.webui.service;

import com.muxin.common.response.AppResponse;
import com.muxin.webui.config.FeignConfig;
import com.muxin.webui.service.impl.OrderServiceFeignException;
import com.muxin.webui.vo.resp.OrderInfoSubmitVo;
import com.muxin.webui.vo.resp.TOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "SCW-ORDER",configuration = FeignConfig.class,fallback = OrderServiceFeignException.class)
public interface OrderServiceFeign {

    @PostMapping("/order/createOrder")
    public AppResponse<TOrder> saveOrder(@RequestBody OrderInfoSubmitVo vo);

}