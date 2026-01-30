package com.ruoyi.mall.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 商城订单
 */
public class MallOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单号(唯一) */
    private String orderNo;

    /** 买家名称 */
    private String buyerName;

    /** 买家手机号 */
    private String buyerPhone;

    /** 收货地址 */
    private String address;

    /** 状态(0待支付,1已支付,2已取消) */
    private Integer status;

    /** 订单总金额 */
    private BigDecimal totalAmount;

    /** 备注 */
    private String remark;

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getBuyerName()
    {
        return buyerName;
    }

    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }

    public String getBuyerPhone()
    {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone)
    {
        this.buyerPhone = buyerPhone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    @Override
    public String getRemark()
    {
        return remark;
    }

    @Override
    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("buyerName", getBuyerName())
            .append("buyerPhone", getBuyerPhone())
            .append("address", getAddress())
            .append("status", getStatus())
            .append("totalAmount", getTotalAmount())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
