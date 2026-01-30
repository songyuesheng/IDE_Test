package com.ruoyi.mall.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.mall.domain.MallOrder;
import com.ruoyi.mall.domain.MallOrderDetail;
import com.ruoyi.mall.domain.MallOrderItem;
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.mapper.MallOrderItemMapper;
import com.ruoyi.mall.mapper.MallOrderMapper;
import com.ruoyi.mall.mapper.MallProductMapper;
import com.ruoyi.mall.service.IMallOrderService;

/**
 * 订单 服务层实现
 */
@Service
public class MallOrderServiceImpl implements IMallOrderService
{
    @Autowired
    private MallOrderMapper orderMapper;

    @Autowired
    private MallOrderItemMapper orderItemMapper;

    @Autowired
    private MallProductMapper productMapper;

    @Override
    public MallOrderDetail selectOrderById(Long orderId)
    {
        MallOrder order = orderMapper.selectOrderById(orderId);
        if (order == null)
        {
            return null;
        }
        MallOrderDetail detail = new MallOrderDetail();
        copyOrder(order, detail);
        detail.setItems(orderItemMapper.selectItemsByOrderId(orderId));
        return detail;
    }

    @Override
    public List<MallOrder> selectOrderList(MallOrder order)
    {
        return orderMapper.selectOrderList(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertOrder(MallOrderDetail orderDetail)
    {
        validateOrder(orderDetail, true);
        if (orderMapper.selectOrderByOrderNo(orderDetail.getOrderNo()) != null)
        {
            throw new ServiceException("订单号已存在");
        }

        List<MallOrderItem> items = buildItems(orderDetail.getItems(), true);
        BigDecimal totalAmount = calcTotal(items);

        orderDetail.setTotalAmount(totalAmount);
        int rows = orderMapper.insertOrder(orderDetail);
        if (rows > 0)
        {
            for (MallOrderItem item : items)
            {
                item.setOrderId(orderDetail.getOrderId());
            }
            orderItemMapper.insertOrderItems(items);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOrder(MallOrderDetail orderDetail)
    {
        if (orderDetail.getOrderId() == null)
        {
            throw new ServiceException("订单ID不能为空");
        }
        MallOrder dbOrder = orderMapper.selectOrderById(orderDetail.getOrderId());
        if (dbOrder == null)
        {
            throw new ServiceException("订单不存在");
        }
        if (StringUtils.isNotEmpty(orderDetail.getOrderNo()))
        {
            MallOrder exists = orderMapper.selectOrderByOrderNo(orderDetail.getOrderNo());
            if (exists != null && !exists.getOrderId().equals(orderDetail.getOrderId()))
            {
                throw new ServiceException("订单号已存在");
            }
        }

        validateOrder(orderDetail, true);
        List<MallOrderItem> items = buildItems(orderDetail.getItems(), false);
        BigDecimal totalAmount = calcTotal(items);
        orderDetail.setTotalAmount(totalAmount);

        int rows = orderMapper.updateOrder(orderDetail);
        orderItemMapper.deleteItemsByOrderId(orderDetail.getOrderId());
        for (MallOrderItem item : items)
        {
            item.setOrderId(orderDetail.getOrderId());
        }
        if (!items.isEmpty())
        {
            orderItemMapper.insertOrderItems(items);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteOrderByIds(Long[] orderIds)
    {
        int rows = 0;
        for (Long orderId : orderIds)
        {
            orderItemMapper.deleteItemsByOrderId(orderId);
            rows += orderMapper.deleteOrderById(orderId);
        }
        return rows;
    }

    private void validateOrder(MallOrderDetail orderDetail, boolean checkItems)
    {
        if (StringUtils.isEmpty(orderDetail.getOrderNo()))
        {
            throw new ServiceException("订单号不能为空");
        }
        if (StringUtils.isEmpty(orderDetail.getBuyerName()))
        {
            throw new ServiceException("买家名称不能为空");
        }
        if (orderDetail.getStatus() == null)
        {
            orderDetail.setStatus(0);
        }
        if (checkItems && (orderDetail.getItems() == null || orderDetail.getItems().isEmpty()))
        {
            throw new ServiceException("订单明细不能为空");
        }
    }

    private List<MallOrderItem> buildItems(List<MallOrderItem> items, boolean reduceStock)
    {
        List<MallOrderItem> results = new ArrayList<>();
        if (items == null)
        {
            return results;
        }
        for (MallOrderItem item : items)
        {
            if (item.getProductId() == null)
            {
                throw new ServiceException("商品ID不能为空");
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0)
            {
                throw new ServiceException("商品数量必须大于0");
            }
            MallProduct product = productMapper.selectProductById(item.getProductId());
            if (product == null)
            {
                throw new ServiceException("商品不存在");
            }
            if (product.getStatus() != null && product.getStatus() == 0)
            {
                throw new ServiceException("商品已下架");
            }
            if (reduceStock)
            {
                int updated = productMapper.reduceStock(product.getProductId(), item.getQuantity());
                if (updated == 0)
                {
                    throw new ServiceException("商品库存不足");
                }
            }
            MallOrderItem newItem = new MallOrderItem();
            newItem.setProductId(product.getProductId());
            newItem.setSku(product.getSku());
            newItem.setProductName(product.getProductName());
            newItem.setUnitPrice(product.getPrice());
            newItem.setQuantity(item.getQuantity());
            newItem.setLineAmount(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            results.add(newItem);
        }
        return results;
    }

    private BigDecimal calcTotal(List<MallOrderItem> items)
    {
        BigDecimal total = BigDecimal.ZERO;
        for (MallOrderItem item : items)
        {
            total = total.add(item.getLineAmount());
        }
        return total;
    }

    private void copyOrder(MallOrder source, MallOrderDetail target)
    {
        target.setOrderId(source.getOrderId());
        target.setOrderNo(source.getOrderNo());
        target.setBuyerName(source.getBuyerName());
        target.setBuyerPhone(source.getBuyerPhone());
        target.setAddress(source.getAddress());
        target.setStatus(source.getStatus());
        target.setTotalAmount(source.getTotalAmount());
        target.setRemark(source.getRemark());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
    }
}
