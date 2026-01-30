package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallOrder;

/**
 * 订单主Service接口
 * 
 * @author ruoyi
 */
public interface IMallOrderService 
{
    /**
     * 查询订单主
     * 
     * @param orderId 订单主主键
     * @return 订单主
     */
    public MallOrder selectMallOrderByOrderId(Long orderId);

    /**
     * 查询订单主列表
     * 
     * @param mallOrder 订单主
     * @return 订单主集合
     */
    public List<MallOrder> selectMallOrderList(MallOrder mallOrder);

    /**
     * 新增订单主
     * 
     * @param mallOrder 订单主
     * @return 结果
     */
    public int insertMallOrder(MallOrder mallOrder);

    /**
     * 修改订单主
     * 
     * @param mallOrder 订单主
     * @return 结果
     */
    public int updateMallOrder(MallOrder mallOrder);

    /**
     * 批量删除订单主
     * 
     * @param orderIds 需要删除的订单主主键集合
     * @return 结果
     */
    public int deleteMallOrderByOrderIds(Long[] orderIds);

    /**
     * 删除订单主信息
     * 
     * @param orderId 订单主主键
     * @return 结果
     */
    public int deleteMallOrderByOrderId(Long orderId);
}
