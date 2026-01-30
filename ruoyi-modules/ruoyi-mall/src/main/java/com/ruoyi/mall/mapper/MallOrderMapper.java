package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallOrder;

/**
 * 订单主Mapper接口
 * 
 * @author ruoyi
 */
public interface MallOrderMapper 
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
     * 删除订单主
     * 
     * @param orderId 订单主主键
     * @return 结果
     */
    public int deleteMallOrderByOrderId(Long orderId);

    /**
     * 批量删除订单主
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMallOrderByOrderIds(Long[] orderIds);
}
