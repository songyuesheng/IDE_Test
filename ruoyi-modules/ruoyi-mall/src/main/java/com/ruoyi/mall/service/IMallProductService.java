package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallProduct;

public interface IMallProductService
{
    public MallProduct selectMallProductById(Long productId);

    public List<MallProduct> selectMallProductList(MallProduct mallProduct);

    public int insertMallProduct(MallProduct mallProduct);

    public int updateMallProduct(MallProduct mallProduct);

    public int deleteMallProductByIds(Long[] productIds);

    public boolean checkSkuUnique(MallProduct mallProduct);

    public int deductStock(Long productId, Integer quantity);
}
