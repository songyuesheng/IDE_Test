package com.ruoyi.mall.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.mall.domain.MallOrder;
import com.ruoyi.mall.domain.MallOrderItem;
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.mapper.MallOrderItemMapper;
import com.ruoyi.mall.mapper.MallOrderMapper;
import com.ruoyi.mall.service.IMallOrderService;
import com.ruoyi.mall.service.IMallProductService;

@Service
public class MallOrderServiceImpl implements IMallOrderService
{
    @Autowired
    private MallOrderMapper mallOrderMapper;

    @Autowired
    private MallOrderItemMapper mallOrderItemMapper;

    @Autowired
    private IMallProductService mallProductService;

    @Override
    public MallOrder selectMallOrderById(Long orderId)
    {
        MallOrder order = mallOrderMapper.selectMallOrderById(orderId);
        if (order != null)
        {
            List<MallOrderItem> items = mallOrderItemMapper.selectMallOrderItemsByOrderId(orderId);
            order.setItems(items);
        }
        return order;
    }

    @Override
    public List<MallOrder> selectMallOrderList(MallOrder mallOrder)
    {
        return mallOrderMapper.selectMallOrderList(mallOrder);
    }

    @Override
    @Transactional
    public int insertMallOrder(MallOrder mallOrder)
    {
        if (mallOrder.getItems() == null || mallOrder.getItems().isEmpty())
        {
            throw new ServiceException("订单明细不能为空");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (MallOrderItem item : mallOrder.getItems())
        {
            MallProduct product = mallProductService.selectMallProductById(item.getProductId());
            if (product == null)
            {
                throw new ServiceException("商品不存在");
            }
            if (product.getStock() < item.getQuantity())
            {
                throw new ServiceException("商品[" + product.getProductName() + "]库存不足");
            }
            item.setSku(product.getSku());
            item.setProductName(product.getProductName());
            item.setUnitPrice(product.getPrice());
            item.setLineAmount(product.getPrice().multiply(new BigDecimal(item.getQuantity())));
            totalAmount = totalAmount.add(item.getLineAmount());

            mallProductService.deductStock(item.getProductId(), item.getQuantity());
        }
        mallOrder.setTotalAmount(totalAmount);

        int rows = mallOrderMapper.insertMallOrder(mallOrder);
        if (rows > 0)
        {
            for (MallOrderItem item : mallOrder.getItems())
            {
                item.setOrderId(mallOrder.getOrderId());
            }
            mallOrderItemMapper.insertMallOrderItemBatch(mallOrder.getItems());
        }
        return rows;
    }

    @Override
    @Transactional
    public int updateMallOrder(MallOrder mallOrder)
    {
        if (mallOrder.getItems() == null || mallOrder.getItems().isEmpty())
        {
            throw new ServiceException("订单明细不能为空");
        }

        MallOrder oldOrder = mallOrderMapper.selectMallOrderById(mallOrder.getOrderId());
        if (oldOrder == null)
        {
            throw new ServiceException("订单不存在");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (MallOrderItem item : mallOrder.getItems())
        {
            MallProduct product = mallProductService.selectMallProductById(item.getProductId());
            if (product == null)
            {
                throw new ServiceException("商品不存在");
            }
            item.setSku(product.getSku());
            item.setProductName(product.getProductName());
            item.setUnitPrice(product.getPrice());
            item.setLineAmount(product.getPrice().multiply(new BigDecimal(item.getQuantity())));
            totalAmount = totalAmount.add(item.getLineAmount());
        }
        mallOrder.setTotalAmount(totalAmount);

        mallOrderItemMapper.deleteMallOrderItemByOrderId(mallOrder.getOrderId());
        for (MallOrderItem item : mallOrder.getItems())
        {
            item.setOrderId(mallOrder.getOrderId());
        }
        mallOrderItemMapper.insertMallOrderItemBatch(mallOrder.getItems());

        return mallOrderMapper.updateMallOrder(mallOrder);
    }

    @Override
    @Transactional
    public int deleteMallOrderByIds(Long[] orderIds)
    {
        for (Long orderId : orderIds)
        {
            mallOrderItemMapper.deleteMallOrderItemByOrderId(orderId);
        }
        return mallOrderMapper.deleteMallOrderByIds(orderIds);
    }

    @Override
    public boolean checkOrderNoUnique(MallOrder mallOrder)
    {
        Long orderId = StringUtils.isNull(mallOrder.getOrderId()) ? -1L : mallOrder.getOrderId();
        MallOrder info = mallOrderMapper.selectMallOrderByOrderNo(mallOrder.getOrderNo());
        if (StringUtils.isNotNull(info) && info.getOrderId().longValue() != orderId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}
