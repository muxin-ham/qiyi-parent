package com.muxin.order.service;

import com.muxin.order.po.TOrder;
import com.muxin.order.vo.req.OrderInfoSubmitVo;

public interface OrderService {
    TOrder saveOrder(OrderInfoSubmitVo vo);
}
