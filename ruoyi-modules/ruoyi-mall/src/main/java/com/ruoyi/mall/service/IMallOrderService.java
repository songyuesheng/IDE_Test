package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallOrder;
import com.ruoyi.mall.domain.MallOrderDetail;

/**
 * 订单 服务层
 */
public interface IMallOrderService
{
    public MallOrderDetail selectOrderById(Long orderId);

    public List<MallOrder> selectOrderList(MallOrder order);

    public int insertOrder(MallOrderDetail orderDetail);

    public int updateOrder(MallOrderDetail orderDetail);

    public int deleteOrderByIds(Long[] orderIds);
}
