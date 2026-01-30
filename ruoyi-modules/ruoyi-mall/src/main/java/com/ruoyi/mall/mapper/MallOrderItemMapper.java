package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallOrderItem;

public interface MallOrderItemMapper
{
    public List<MallOrderItem> selectMallOrderItemsByOrderId(Long orderId);

    public int insertMallOrderItem(MallOrderItem mallOrderItem);

    public int insertMallOrderItemBatch(List<MallOrderItem> mallOrderItems);

    public int updateMallOrderItem(MallOrderItem mallOrderItem);

    public int deleteMallOrderItemByOrderId(Long orderId);

    public int deleteMallOrderItemById(Long itemId);
}
