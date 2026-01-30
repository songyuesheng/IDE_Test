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
import com.ruoyi.mall.domain.MallOrder;
import com.ruoyi.mall.service.IMallOrderService;

@RestController
@RequestMapping("/order")
public class MallOrderController extends BaseController
{
    @Autowired
    private IMallOrderService mallOrderService;

    @RequiresPermissions("mall:order:list")
    @GetMapping("/list")
    public TableDataInfo list(MallOrder mallOrder)
    {
        startPage();
        List<MallOrder> list = mallOrderService.selectMallOrderList(mallOrder);
        return getDataTable(list);
    }

    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable Long orderId)
    {
        return success(mallOrderService.selectMallOrderById(orderId));
    }

    @RequiresPermissions("mall:order:add")
    @Log(title = "订单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MallOrder mallOrder)
    {
        if (!mallOrderService.checkOrderNoUnique(mallOrder))
        {
            return error("新增订单失败，订单号已存在");
        }
        mallOrder.setCreateBy(SecurityUtils.getUsername());
        return toAjax(mallOrderService.insertMallOrder(mallOrder));
    }

    @RequiresPermissions("mall:order:edit")
    @Log(title = "订单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MallOrder mallOrder)
    {
        if (!mallOrderService.checkOrderNoUnique(mallOrder))
        {
            return error("修改订单失败，订单号已存在");
        }
        mallOrder.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(mallOrderService.updateMallOrder(mallOrder));
    }

    @RequiresPermissions("mall:order:remove")
    @Log(title = "订单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(mallOrderService.deleteMallOrderByIds(orderIds));
    }
}
