package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallOrder;

public interface MallOrderMapper
{
    public MallOrder selectMallOrderById(Long orderId);

    public MallOrder selectMallOrderByOrderNo(String orderNo);

    public List<MallOrder> selectMallOrderList(MallOrder mallOrder);

    public int insertMallOrder(MallOrder mallOrder);

    public int updateMallOrder(MallOrder mallOrder);

    public int deleteMallOrderById(Long orderId);

    public int deleteMallOrderByIds(Long[] orderIds);
}
