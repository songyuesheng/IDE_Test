package com.ruoyi.mall.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 订单明细对象 mall_order_item
 * 
 * @author ruoyi
 */
public class MallOrderItem
{
    private static final long serialVersionUID = 1L;

    /** 明细ID */
    private Long itemId;

    /** 订单ID */
    private Long orderId;

    /** 商品ID */
    private Long productId;

    /** SKU */
    @Excel(name = "SKU")
    private String sku;

    /** 商品名称 */
    @Excel(name = "商品名称")
    private String productName;

    /** 下单单价 */
    @Excel(name = "下单单价")
    private BigDecimal unitPrice;

    /** 数量 */
    @Excel(name = "数量")
    private Integer quantity;

    /** 行金额 */
    @Excel(name = "行金额")
    private BigDecimal lineAmount;

    public void setItemId(Long itemId) 
    {
        this.itemId = itemId;
    }

    public Long getItemId() 
    {
        return itemId;
    }
    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }
    public void setProductId(Long productId) 
    {
        this.productId = productId;
    }

    public Long getProductId() 
    {
        return productId;
    }
    public void setSku(String sku) 
    {
        this.sku = sku;
    }

    public String getSku() 
    {
        return sku;
    }
    public void setProductName(String productName) 
    {
        this.productName = productName;
    }

    public String getProductName() 
    {
        return productName;
    }
    public void setUnitPrice(BigDecimal unitPrice) 
    {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() 
    {
        return unitPrice;
    }
    public void setQuantity(Integer quantity) 
    {
        this.quantity = quantity;
    }

    public Integer getQuantity() 
    {
        return quantity;
    }
    public void setLineAmount(BigDecimal lineAmount) 
    {
        this.lineAmount = lineAmount;
    }

    public BigDecimal getLineAmount() 
    {
        return lineAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("itemId", getItemId())
            .append("orderId", getOrderId())
            .append("productId", getProductId())
            .append("sku", getSku())
            .append("productName", getProductName())
            .append("unitPrice", getUnitPrice())
            .append("quantity", getQuantity())
            .append("lineAmount", getLineAmount())
            .toString();
    }
}
