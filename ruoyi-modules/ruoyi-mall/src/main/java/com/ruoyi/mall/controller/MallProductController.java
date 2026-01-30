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
import com.ruoyi.mall.domain.MallProduct;
import com.ruoyi.mall.service.IMallProductService;

/**
 * 商品管理
 */
@RestController
@RequestMapping("/product")
public class MallProductController extends BaseController
{
    @Autowired
    private IMallProductService productService;

    /**
     * 商品列表
     */
    @RequiresPermissions("mall:product:list")
    @GetMapping("/list")
    public TableDataInfo list(MallProduct product)
    {
        startPage();
        List<MallProduct> list = productService.selectProductList(product);
        return getDataTable(list);
    }

    /**
     * 商品详情
     */
    @RequiresPermissions("mall:product:query")
    @GetMapping(value = "/{productId}")
    public AjaxResult getInfo(@PathVariable Long productId)
    {
        return success(productService.selectProductById(productId));
    }

    /**
     * 新增商品
     */
    @RequiresPermissions("mall:product:add")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MallProduct product)
    {
        return toAjax(productService.insertProduct(product));
    }

    /**
     * 修改商品
     */
    @RequiresPermissions("mall:product:edit")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MallProduct product)
    {
        return toAjax(productService.updateProduct(product));
    }

    /**
     * 删除商品(逻辑删除)
     */
    @RequiresPermissions("mall:product:remove")
    @Log(title = "商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds)
    {
        return toAjax(productService.deleteProductByIds(productIds));
    }
}
