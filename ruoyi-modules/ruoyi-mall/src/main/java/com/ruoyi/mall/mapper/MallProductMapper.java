package com.ruoyi.mall.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.mall.domain.MallProduct;

/**
 * 商品 数据层
 */
public interface MallProductMapper
{
    public MallProduct selectProductById(Long productId);

    public MallProduct selectProductBySku(String sku);

    public List<MallProduct> selectProductList(MallProduct product);

    public int insertProduct(MallProduct product);

    public int updateProduct(MallProduct product);

    public int deleteProductByIds(Long[] productIds);

    public int reduceStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
