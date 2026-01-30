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
import com.ruoyi.mall.domain.MallOrder;
import com.ruoyi.mall.domain.MallOrderDetail;
import com.ruoyi.mall.service.IMallOrderService;

/**
 * 订单管理
 */
@RestController
@RequestMapping("/order")
public class MallOrderController extends BaseController
{
    @Autowired
    private IMallOrderService orderService;

    /**
     * 订单列表
     */
    @RequiresPermissions("mall:order:list")
    @GetMapping("/list")
    public TableDataInfo list(MallOrder order)
    {
        startPage();
        List<MallOrder> list = orderService.selectOrderList(order);
        return getDataTable(list);
    }

    /**
     * 订单详情
     */
    @RequiresPermissions("mall:order:query")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable Long orderId)
    {
        return success(orderService.selectOrderById(orderId));
    }

    /**
     * 新增订单
     */
    @RequiresPermissions("mall:order:add")
    @Log(title = "订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody MallOrderDetail orderDetail)
    {
        return toAjax(orderService.insertOrder(orderDetail));
    }

    /**
     * 修改订单
     */
    @RequiresPermissions("mall:order:edit")
    @Log(title = "订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody MallOrderDetail orderDetail)
    {
        return toAjax(orderService.updateOrder(orderDetail));
    }

    /**
     * 删除订单
     */
    @RequiresPermissions("mall:order:remove")
    @Log(title = "订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(orderService.deleteOrderByIds(orderIds));
    }
}
