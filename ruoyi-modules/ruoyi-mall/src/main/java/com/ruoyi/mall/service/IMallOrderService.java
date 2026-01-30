package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallOrder;

public interface IMallOrderService
{
    public MallOrder selectMallOrderById(Long orderId);

    public List<MallOrder> selectMallOrderList(MallOrder mallOrder);

    public int insertMallOrder(MallOrder mallOrder);

    public int updateMallOrder(MallOrder mallOrder);

    public int deleteMallOrderByIds(Long[] orderIds);

    public boolean checkOrderNoUnique(MallOrder mallOrder);
}
