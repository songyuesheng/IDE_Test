package com.ruoyi.mall.domain;

import java.util.List;

/**
 * 订单详情
 */
public class MallOrderDetail extends MallOrder
{
    private static final long serialVersionUID = 1L;

    /** 订单明细 */
    private List<MallOrderItem> items;

    public List<MallOrderItem> getItems()
    {
        return items;
    }

    public void setItems(List<MallOrderItem> items)
    {
        this.items = items;
    }
}
