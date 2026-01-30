package com.ruoyi.mall.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;
import com.ruoyi.common.core.web.domain.BaseEntity;

public class MallOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "订单ID", cellType = ColumnType.NUMERIC)
    private Long orderId;

    @Excel(name = "订单号")
    @NotBlank(message = "订单号不能为空")
    @Size(max = 64, message = "订单号长度不能超过64个字符")
    private String orderNo;

    @Excel(name = "买家名称")
    @NotBlank(message = "买家名称不能为空")
    @Size(max = 64, message = "买家名称长度不能超过64个字符")
    private String buyerName;

    @Excel(name = "买家手机号")
    @Size(max = 32, message = "买家手机号长度不能超过32个字符")
    private String buyerPhone;

    @Excel(name = "收货地址")
    @Size(max = 256, message = "收货地址长度不能超过256个字符")
    private String address;

    @Excel(name = "状态", readConverterExp = "0=待支付,1=已支付,2=已取消")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Excel(name = "订单总金额")
    @NotNull(message = "订单总金额不能为空")
    private BigDecimal totalAmount;

    private List<MallOrderItem> items;

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

    public List<MallOrderItem> getItems()
    {
        return items;
    }

    public void setItems(List<MallOrderItem> items)
    {
        this.items = items;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
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
