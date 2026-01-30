package com.ruoyi.mall.mapper;

import java.util.List;
import com.ruoyi.mall.domain.MallProduct;

public interface MallProductMapper
{
    public MallProduct selectMallProductById(Long productId);

    public MallProduct selectMallProductBySku(String sku);

    public List<MallProduct> selectMallProductList(MallProduct mallProduct);

    public int insertMallProduct(MallProduct mallProduct);

    public int updateMallProduct(MallProduct mallProduct);

    public int deleteMallProductById(Long productId);

    public int deleteMallProductByIds(Long[] productIds);

    public int updateStock(Long productId, Integer quantity);
}
