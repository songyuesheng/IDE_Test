package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallProduct;

/**
 * 商品 服务层
 */
public interface IMallProductService
{
    public MallProduct selectProductById(Long productId);

    public List<MallProduct> selectProductList(MallProduct product);

    public int insertProduct(MallProduct product);

    public int updateProduct(MallProduct product);

    public int deleteProductByIds(Long[] productIds);
}
