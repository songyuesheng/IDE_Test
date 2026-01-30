package com.ruoyi.mall.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.service.IMallProductService;

@RestController
@RequestMapping("/product")
public class MallProductController extends BaseController
{
    @Autowired
    private IMallProductService mallProductService;

    @RequiresPermissions("mall:product:list")
    @GetMapping("/list")
    public TableDataInfo list(MallProduct mallProduct)
    {
        startPage();
        List<MallProduct> list = mallProductService.selectMallProductList(mallProduct);
        return getDataTable(list);
    }

    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable Long productId)
    {
        return success(mallProductService.selectMallProductById(productId));
    }

    @RequiresPermissions("mall:product:add")
    @Log(title = "商品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MallProduct mallProduct)
    {
        if (!mallProductService.checkSkuUnique(mallProduct))
        {
            return error("新增商品'" + mallProduct.getProductName() + "'失败，SKU已存在");
        }
        mallProduct.setCreateBy(SecurityUtils.getUsername());
        return toAjax(mallProductService.insertMallProduct(mallProduct));
    }

    @RequiresPermissions("mall:product:edit")
    @Log(title = "商品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MallProduct mallProduct)
    {
        if (!mallProductService.checkSkuUnique(mallProduct))
        {
            return error("修改商品'" + mallProduct.getProductName() + "'失败，SKU已存在");
        }
        mallProduct.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(mallProductService.updateMallProduct(mallProduct));
    }

    @RequiresPermissions("mall:product:remove")
    @Log(title = "商品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        return toAjax(mallProductService.deleteMallProductByIds(productIds));
    }
}
