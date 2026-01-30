package com.ruoyi.mall.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.mall.domain.MallOrderItem;
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.mapper.MallOrderMapper;
import com.ruoyi.mall.mapper.MallOrderItemMapper;
import com.ruoyi.mall.mapper.MallProductMapper;
import com.ruoyi.mall.domain.MallOrder;
import com.ruoyi.mall.service.IMallOrderService;

/**
 * 订单主Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class MallOrderServiceImpl implements IMallOrderService 
{
    @Autowired
    private MallOrderMapper mallOrderMapper;
    
    @Autowired
    private MallOrderItemMapper mallOrderItemMapper;

    @Autowired
    private MallProductMapper mallProductMapper;

    /**
     * 查询订单主
     * 
     * @param orderId 订单主主键
     * @return 订单主
     */
    @Override
    public MallOrder selectMallOrderByOrderId(Long orderId)
    {
        return mallOrderMapper.selectMallOrderByOrderId(orderId);
    }

    /**
     * 查询订单主列表
     * 
     * @param mallOrder 订单主
     * @return 订单主
     */
    @Override
    public List<MallOrder> selectMallOrderList(MallOrder mallOrder)
    {
        return mallOrderMapper.selectMallOrderList(mallOrder);
    }

    /**
     * 新增订单主
     * 
     * @param mallOrder 订单主
     * @return 结果
     */
    @Transactional
    @Override
    public int insertMallOrder(MallOrder mallOrder)
    {
        mallOrder.setCreateTime(DateUtils.getNowDate());
        List<MallOrderItem> mallOrderItemList = mallOrder.getMallOrderItemList();
        BigDecimal totalAmount = BigDecimal.ZERO;

        if (StringUtils.isNotEmpty(mallOrderItemList)) {
            for (MallOrderItem item : mallOrderItemList) {
                // 校验库存并扣减
                MallProduct product = mallProductMapper.selectMallProductByProductId(item.getProductId());
                if (product == null) {
                    throw new ServiceException("商品ID " + item.getProductId() + " 不存在");
                }
                if (product.getStatus() == 0) { // 假设0是下架
                     throw new ServiceException("商品 " + product.getProductName() + " 已下架");
                }
                if (product.getStock() < item.getQuantity()) {
                    throw new ServiceException("商品 " + product.getProductName() + " 库存不足");
                }
                
                // 扣减库存
                product.setStock(product.getStock() - item.getQuantity());
                mallProductMapper.updateMallProduct(product);

                // 计算金额
                // 使用商品当前价格或前端传递的价格? 题目说前端"选择商品后自动回填...数量变更自动计算line_amount"，
                // 但后端"新增...计算金额"。为了安全通常后端取价格。这里假设使用前端传的单价但校验? 
                // 或者直接使用数据库价格。这里选择使用前端传递的单价(灵活性)或数据库(安全性)。
                // 题目要求"自动回填"，暗示前端有价格。后端"计算金额"可能指TotalAmount。
                // 我们重新计算 LineAmount 和 TotalAmount 以确保准确。
                BigDecimal price = item.getUnitPrice(); // Trust frontend or fetch from DB? Let's trust frontend for unit price but re-calc totals.
                if (price == null) price = product.getPrice();
                
                BigDecimal lineAmount = price.multiply(new BigDecimal(item.getQuantity()));
                item.setLineAmount(lineAmount);
                item.setUnitPrice(price);
                item.setProductName(product.getProductName()); // Redundant storage
                item.setSku(product.getSku()); // Redundant

                totalAmount = totalAmount.add(lineAmount);
            }
        }
        mallOrder.setTotalAmount(totalAmount);
        
        int rows = mallOrderMapper.insertMallOrder(mallOrder);
        insertMallOrderItem(mallOrder);
        return rows;
    }

    /**
     * 修改订单主
     * 
     * @param mallOrder 订单主
     * @return 结果
     */
    @Transactional
    @Override
    public int updateMallOrder(MallOrder mallOrder)
    {
        mallOrder.setUpdateTime(DateUtils.getNowDate());
        mallOrderItemMapper.deleteMallOrderItemByOrderIds(new Long[]{mallOrder.getOrderId()});
        
        List<MallOrderItem> mallOrderItemList = mallOrder.getMallOrderItemList();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        if (StringUtils.isNotEmpty(mallOrderItemList)) {
            for (MallOrderItem item : mallOrderItemList) {
                BigDecimal lineAmount = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
                item.setLineAmount(lineAmount);
                totalAmount = totalAmount.add(lineAmount);
            }
        }
        mallOrder.setTotalAmount(totalAmount);
        
        insertMallOrderItem(mallOrder);
        return mallOrderMapper.updateMallOrder(mallOrder);
    }

    /**
     * 批量删除订单主
     * 
     * @param orderIds 需要删除的订单主主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteMallOrderByOrderIds(Long[] orderIds)
    {
        mallOrderItemMapper.deleteMallOrderItemByOrderIds(orderIds);
        return mallOrderMapper.deleteMallOrderByOrderIds(orderIds);
    }

    /**
     * 删除订单主信息
     * 
     * @param orderId 订单主主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteMallOrderByOrderId(Long orderId)
    {
        mallOrderItemMapper.deleteMallOrderItemByOrderIds(new Long[]{orderId});
        return mallOrderMapper.deleteMallOrderByOrderId(orderId);
    }

    /**
     * 新增订单明细信息
     * 
     * @param mallOrder 订单主对象
     */
    public void insertMallOrderItem(MallOrder mallOrder)
    {
        List<MallOrderItem> mallOrderItemList = mallOrder.getMallOrderItemList();
        Long orderId = mallOrder.getOrderId();
        if (StringUtils.isNotNull(mallOrderItemList))
        {
            List<MallOrderItem> list = new ArrayList<MallOrderItem>();
            for (MallOrderItem mallOrderItem : mallOrderItemList)
            {
                mallOrderItem.setOrderId(orderId);
                list.add(mallOrderItem);
            }
            if (list.size() > 0)
            {
                mallOrderItemMapper.batchMallOrderItem(list);
            }
        }
    }
}
