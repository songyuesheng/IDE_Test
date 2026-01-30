package com.ruoyi.mall.domain;

import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

/**
 * 订单主对象 mall_order
 * 
 * @author ruoyi
 */
public class MallOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单号 */
    @Excel(name = "订单号")
    private String orderNo;

    /** 买家名称 */
    @Excel(name = "买家名称")
    private String buyerName;

    /** 买家手机号 */
    @Excel(name = "买家手机号")
    private String buyerPhone;

    /** 收货地址 */
    @Excel(name = "收货地址")
    private String address;

    /** 状态(0待支付 1已支付 2已取消) */
    @Excel(name = "状态", readConverterExp = "0=待支付,1=已支付,2=已取消")
    private Integer status;

    /** 订单总金额 */
    @Excel(name = "订单总金额")
    private BigDecimal totalAmount;

    /** 订单明细信息 */
    private List<MallOrderItem> mallOrderItemList;

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }
    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setBuyerName(String buyerName) 
    {
        this.buyerName = buyerName;
    }

    public String getBuyerName() 
    {
        return buyerName;
    }
    public void setBuyerPhone(String buyerPhone) 
    {
        this.buyerPhone = buyerPhone;
    }

    public String getBuyerPhone() 
    {
        return buyerPhone;
    }
    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setTotalAmount(BigDecimal totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() 
    {
        return totalAmount;
    }

    public List<MallOrderItem> getMallOrderItemList()
    {
        return mallOrderItemList;
    }

    public void setMallOrderItemList(List<MallOrderItem> mallOrderItemList)
    {
        this.mallOrderItemList = mallOrderItemList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
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
            .append("mallOrderItemList", getMallOrderItemList())
            .toString();
    }
}
