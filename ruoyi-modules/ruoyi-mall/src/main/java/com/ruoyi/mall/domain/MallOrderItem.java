package com.ruoyi.mall.domain;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;
import com.ruoyi.common.core.web.domain.BaseEntity;

public class MallOrderItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "明细ID", cellType = ColumnType.NUMERIC)
    private Long itemId;

    @Excel(name = "订单ID", cellType = ColumnType.NUMERIC)
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Excel(name = "商品ID", cellType = ColumnType.NUMERIC)
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @Excel(name = "SKU")
    private String sku;

    @Excel(name = "商品名称")
    private String productName;

    @Excel(name = "下单单价")
    @NotNull(message = "下单单价不能为空")
    private BigDecimal unitPrice;

    @Excel(name = "数量")
    @NotNull(message = "数量不能为空")
    private Integer quantity;

    @Excel(name = "行金额")
    @NotNull(message = "行金额不能为空")
    private BigDecimal lineAmount;

    public Long getItemId()
    {
        return itemId;
    }

    public void setItemId(Long itemId)
    {
        this.itemId = itemId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getProductId()
    {
        return productId;
    }

    public void setProductId(Long productId)
    {
        this.productId = productId;
    }

    public String getSku()
    {
        return sku;
    }

    public void setSku(String sku)
    {
        this.sku = sku;
    }

    public String getProductName()
    {
        return productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public BigDecimal getLineAmount()
    {
        return lineAmount;
    }

    public void setLineAmount(BigDecimal lineAmount)
    {
        this.lineAmount = lineAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
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
