package com.ruoyi.mall.service;

import java.util.List;
import com.ruoyi.mall.domain.MallProduct;

/**
 * 商品Service接口
 * 
 * @author ruoyi
 */
public interface IMallProductService 
{
    /**
     * 查询商品
     * 
     * @param productId 商品主键
     * @return 商品
     */
    public MallProduct selectMallProductByProductId(Long productId);

    /**
     * 查询商品列表
     * 
     * @param mallProduct 商品
     * @return 商品集合
     */
    public List<MallProduct> selectMallProductList(MallProduct mallProduct);

    /**
     * 新增商品
     * 
     * @param mallProduct 商品
     * @return 结果
     */
    public int insertMallProduct(MallProduct mallProduct);

    /**
     * 修改商品
     * 
     * @param mallProduct 商品
     * @return 结果
     */
    public int updateMallProduct(MallProduct mallProduct);

    /**
     * 批量删除商品
     * 
     * @param productIds 需要删除的商品主键集合
     * @return 结果
     */
    public int deleteMallProductByProductIds(Long[] productIds);

    /**
     * 删除商品信息
     * 
     * @param productId 商品主键
     * @return 结果
     */
    public int deleteMallProductByProductId(Long productId);

    /**
     * 校验SKU是否唯一
     * 
     * @param mallProduct 商品信息
     * @return 结果
     */
    public boolean checkSkuUnique(MallProduct mallProduct);
}
