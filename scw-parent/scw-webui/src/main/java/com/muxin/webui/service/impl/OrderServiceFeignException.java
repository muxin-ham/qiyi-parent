package com.muxin.webui.service.impl;

import com.muxin.common.response.AppResponse;
import com.muxin.webui.service.OrderServiceFeign;
import com.muxin.webui.vo.resp.OrderInfoSubmitVo;
import com.muxin.webui.vo.resp.TOrder;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceFeignException implements OrderServiceFeign {
    @Override
    public AppResponse<TOrder> saveOrder(OrderInfoSubmitVo vo) {
        AppResponse<TOrder> response = AppResponse.fail(null);
        response.setMsg("远程调用失败【订单】");
        return response;
    }
}
