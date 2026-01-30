package com.ruoyi.mall.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.mapper.MallProductMapper;
import com.ruoyi.mall.service.IMallProductService;

/**
 * 商品 服务层实现
 */
@Service
public class MallProductServiceImpl implements IMallProductService
{
    @Autowired
    private MallProductMapper productMapper;

    @Override
    public MallProduct selectProductById(Long productId)
    {
        return productMapper.selectProductById(productId);
    }

    @Override
    public List<MallProduct> selectProductList(MallProduct product)
    {
        return productMapper.selectProductList(product);
    }

    @Override
    public int insertProduct(MallProduct product)
    {
        validateProduct(product);
        checkSkuUnique(product.getSku(), null);
        return productMapper.insertProduct(product);
    }

    @Override
    public int updateProduct(MallProduct product)
    {
        if (product.getProductId() == null)
        {
            throw new ServiceException("商品ID不能为空");
        }
        validateProduct(product);
        checkSkuUnique(product.getSku(), product.getProductId());
        return productMapper.updateProduct(product);
    }

    @Override
    public int deleteProductByIds(Long[] productIds)
    {
        return productMapper.deleteProductByIds(productIds);
    }

    private void validateProduct(MallProduct product)
    {
        if (StringUtils.isEmpty(product.getSku()))
        {
            throw new ServiceException("SKU不能为空");
        }
        if (StringUtils.isEmpty(product.getProductName()))
        {
            throw new ServiceException("商品名称不能为空");
        }
        if (product.getPrice() == null)
        {
            product.setPrice(BigDecimal.ZERO);
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0)
        {
            throw new ServiceException("单价不能小于0");
        }
        if (product.getStock() == null)
        {
            product.setStock(0);
        }
        if (product.getStock() < 0)
        {
            throw new ServiceException("库存不能小于0");
        }
        if (product.getStatus() == null)
        {
            product.setStatus(1);
        }
    }

    private void checkSkuUnique(String sku, Long productId)
    {
        MallProduct exists = productMapper.selectProductBySku(sku);
        if (exists != null && (productId == null || !productId.equals(exists.getProductId())))
        {
            throw new ServiceException("SKU已存在");
        }
    }
}
