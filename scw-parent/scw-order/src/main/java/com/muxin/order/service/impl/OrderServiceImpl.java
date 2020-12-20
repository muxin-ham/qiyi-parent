package com.muxin.order.service.impl;

import com.muxin.common.enums.OrderStatusEnum;
import com.muxin.common.response.AppResponse;
import com.muxin.order.mapper.TOrderMapper;
import com.muxin.order.po.TOrder;
import com.muxin.order.service.OrderService;
import com.muxin.order.service.ProjectServiceFeign;
import com.muxin.order.vo.req.OrderInfoSubmitVo;
import com.muxin.order.vo.resp.TReturn;
import com.muxin.utils.AppDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {
        /*1、权限验证，获取id*/
        String accessToken = vo.getAccessToken();
        String memberId = redisTemplate.opsForValue().get(accessToken);
        /*2、vo转po*/
        TOrder order = new TOrder();
        order.setMemberid(Integer.parseInt(memberId));
        //项目ID
        order.setProjectid(vo.getProjectid());
        //回报项目ID
        order.setReturnid(vo.getReturnid());
        //订单创建时间
        order.setCreatedate(AppDateUtils.getFormatTime());
        //生成订单编号
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        order.setOrdernum(orderNum);
        /*3、order微服务调用project微服务*/
        AppResponse<List<TReturn>> returnInfo = projectServiceFeign.returnInfo(vo.getProjectid());
        List<TReturn> tReturnList = returnInfo.getData();
        TReturn tReturn = tReturnList.get(0);
        //计算回报金额   支持数量*支持金额+运费
        Integer totalMoney = vo.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
        order.setMoney(totalMoney);
        //回报数量
        order.setRtncount(vo.getRtncount());
        //支付状态  未支付
        order.setStatus(OrderStatusEnum.UNPAY.getCode()+"");
        //收货地址
        order.setAddress(vo.getAddress());
        //是否开发票
        order.setInvoice(vo.getInvoice().toString());
        //发票名头
        order.setInvoictitle(vo.getInvoictitle());
        //备注
        order.setRemark(vo.getRemark());
        orderMapper.insertSelective(order);
        return order;
    }
}
