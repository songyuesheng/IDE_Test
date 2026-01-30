package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallOrder;

/**
 * 订单 数据层
 */
public interface MallOrderMapper
{
    public MallOrder selectOrderById(Long orderId);

    public MallOrder selectOrderByOrderNo(String orderNo);

    public List<MallOrder> selectOrderList(MallOrder order);

    public int insertOrder(MallOrder order);

    public int updateOrder(MallOrder order);

    public int deleteOrderById(Long orderId);

    public int deleteOrderByIds(Long[] orderIds);
}
