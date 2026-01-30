package com.ruoyi.mall.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.mall.mapper.MallProductMapper;
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.service.IMallProductService;

/**
 * 商品Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class MallProductServiceImpl implements IMallProductService 
{
    @Autowired
    private MallProductMapper mallProductMapper;

    /**
     * 查询商品
     * 
     * @param productId 商品主键
     * @return 商品
     */
    @Override
    public MallProduct selectMallProductByProductId(Long productId)
    {
        return mallProductMapper.selectMallProductByProductId(productId);
    }

    /**
     * 查询商品列表
     * 
     * @param mallProduct 商品
     * @return 商品
     */
    @Override
    public List<MallProduct> selectMallProductList(MallProduct mallProduct)
    {
        return mallProductMapper.selectMallProductList(mallProduct);
    }

    /**
     * 新增商品
     * 
     * @param mallProduct 商品
     * @return 结果
     */
    @Override
    public int insertMallProduct(MallProduct mallProduct)
    {
        // 校验
        if (!checkSkuUnique(mallProduct)) {
            throw new ServiceException("新增商品'" + mallProduct.getProductName() + "'失败，SKU已存在");
        }
        if (mallProduct.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("新增商品'" + mallProduct.getProductName() + "'失败，价格不能小于0");
        }
        if (mallProduct.getStock() < 0) {
            throw new ServiceException("新增商品'" + mallProduct.getProductName() + "'失败，库存不能小于0");
        }

        mallProduct.setCreateTime(DateUtils.getNowDate());
        return mallProductMapper.insertMallProduct(mallProduct);
    }

    /**
     * 修改商品
     * 
     * @param mallProduct 商品
     * @return 结果
     */
    @Override
    public int updateMallProduct(MallProduct mallProduct)
    {
        if (!checkSkuUnique(mallProduct)) {
            throw new ServiceException("修改商品'" + mallProduct.getProductName() + "'失败，SKU已存在");
        }
        if (mallProduct.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("修改商品'" + mallProduct.getProductName() + "'失败，价格不能小于0");
        }
        if (mallProduct.getStock() < 0) {
            throw new ServiceException("修改商品'" + mallProduct.getProductName() + "'失败，库存不能小于0");
        }
        mallProduct.setUpdateTime(DateUtils.getNowDate());
        return mallProductMapper.updateMallProduct(mallProduct);
    }

    /**
     * 批量删除商品
     * 
     * @param productIds 需要删除的商品主键
     * @return 结果
     */
    @Override
    public int deleteMallProductByProductIds(Long[] productIds)
    {
        return mallProductMapper.deleteMallProductByProductIds(productIds);
    }

    /**
     * 删除商品信息
     * 
     * @param productId 商品主键
     * @return 结果
     */
    @Override
    public int deleteMallProductByProductId(Long productId)
    {
        return mallProductMapper.deleteMallProductByProductId(productId);
    }

    /**
     * 校验SKU是否唯一
     */
    @Override
    public boolean checkSkuUnique(MallProduct mallProduct) {
        Long productId = StringUtils.isNull(mallProduct.getProductId()) ? -1L : mallProduct.getProductId();
        MallProduct info = mallProductMapper.checkSkuUnique(mallProduct.getSku());
        if (StringUtils.isNotNull(info) && info.getProductId().longValue() != productId.longValue()) {
            return false;
        }
        return true;
    }
}
