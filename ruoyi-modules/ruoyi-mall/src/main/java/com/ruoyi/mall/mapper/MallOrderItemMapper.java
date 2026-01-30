package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallOrderItem;

/**
 * 订单明细 数据层
 */
public interface MallOrderItemMapper
{
    public List<MallOrderItem> selectItemsByOrderId(Long orderId);

    public int insertOrderItems(List<MallOrderItem> items);

    public int deleteItemsByOrderId(Long orderId);
}
