package com.ruoyi.mall.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.mapper.MallProductMapper;
import com.ruoyi.mall.service.IMallProductService;

@Service
public class MallProductServiceImpl implements IMallProductService
{
    @Autowired
    private MallProductMapper mallProductMapper;

    @Override
    public MallProduct selectMallProductById(Long productId)
    {
        return mallProductMapper.selectMallProductById(productId);
    }

    @Override
    public List<MallProduct> selectMallProductList(MallProduct mallProduct)
    {
        return mallProductMapper.selectMallProductList(mallProduct);
    }

    @Override
    public int insertMallProduct(MallProduct mallProduct)
    {
        if (mallProduct.getPrice().compareTo(BigDecimal.ZERO) < 0)
        {
            throw new ServiceException("商品单价不能小于0");
        }
        if (mallProduct.getStock() < 0)
        {
            throw new ServiceException("商品库存不能小于0");
        }
        return mallProductMapper.insertMallProduct(mallProduct);
    }

    @Override
    public int updateMallProduct(MallProduct mallProduct)
    {
        if (mallProduct.getPrice() != null && mallProduct.getPrice().compareTo(BigDecimal.ZERO) < 0)
        {
            throw new ServiceException("商品单价不能小于0");
        }
        if (mallProduct.getStock() != null && mallProduct.getStock() < 0)
        {
            throw new ServiceException("商品库存不能小于0");
        }
        return mallProductMapper.updateMallProduct(mallProduct);
    }

    @Override
    public int deleteMallProductByIds(Long[] productIds)
    {
        return mallProductMapper.deleteMallProductByIds(productIds);
    }

    @Override
    public boolean checkSkuUnique(MallProduct mallProduct)
    {
        Long productId = StringUtils.isNull(mallProduct.getProductId()) ? -1L : mallProduct.getProductId();
        MallProduct info = mallProductMapper.selectMallProductBySku(mallProduct.getSku());
        if (StringUtils.isNotNull(info) && info.getProductId().longValue() != productId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public int deductStock(Long productId, Integer quantity)
    {
        return mallProductMapper.updateStock(productId, quantity);
    }
}
