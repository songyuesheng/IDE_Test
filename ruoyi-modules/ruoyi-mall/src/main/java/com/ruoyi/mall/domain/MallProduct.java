package com.ruoyi.mall.domain;

import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.annotation.Excel.ColumnType;
import com.ruoyi.common.core.web.domain.BaseEntity;

public class MallProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "商品ID", cellType = ColumnType.NUMERIC)
    private Long productId;

    @Excel(name = "SKU")
    @NotBlank(message = "SKU不能为空")
    @Size(max = 64, message = "SKU长度不能超过64个字符")
    private String sku;

    @Excel(name = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 128, message = "商品名称长度不能超过128个字符")
    private String productName;

    @Excel(name = "类目")
    @Size(max = 64, message = "类目长度不能超过64个字符")
    private String category;

    @Excel(name = "单价")
    @NotNull(message = "单价不能为空")
    private BigDecimal price;

    @Excel(name = "库存")
    @NotNull(message = "库存不能为空")
    private Integer stock;

    @Excel(name = "状态", readConverterExp = "0=下架,1=上架")
    @NotNull(message = "状态不能为空")
    private Integer status;

    private String delFlag;

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

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public Integer getStock()
    {
        return stock;
    }

    public void setStock(Integer stock)
    {
        this.stock = stock;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("productId", getProductId())
            .append("sku", getSku())
            .append("productName", getProductName())
            .append("category", getCategory())
            .append("price", getPrice())
            .append("stock", getStock())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
