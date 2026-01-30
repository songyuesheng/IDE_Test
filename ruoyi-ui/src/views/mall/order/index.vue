<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="买家" prop="buyerName">
        <el-input
          v-model="queryParams.buyerName"
          placeholder="请输入买家名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['mall:order:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['mall:order:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mall:order:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="orderId" width="80" />
      <el-table-column label="订单号" align="center" prop="orderNo" width="160" />
      <el-table-column label="买家" align="center" prop="buyerName" width="120" />
      <el-table-column label="手机号" align="center" prop="buyerPhone" width="130" />
      <el-table-column label="地址" align="center" prop="address" :show-overflow-tooltip="true" />
      <el-table-column label="状态" align="center" prop="status" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="success">已支付</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="danger">已取消</el-tag>
          <el-tag v-else type="warning">待支付</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="center" prop="totalAmount" width="110" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['mall:order:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mall:order:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mall:order:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" :visible.sync="open" width="980px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="订单号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="请输入订单号" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="买家" prop="buyerName">
              <el-input v-model="form.buyerName" placeholder="请输入买家名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="手机号" prop="buyerPhone">
              <el-input v-model="form.buyerPhone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入收货地址" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-divider>订单明细</el-divider>
      <el-table :data="items" border>
        <el-table-column label="商品" width="240">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.productId"
              placeholder="选择商品"
              filterable
              style="width: 100%"
              @change="onProductChange(scope.row)"
            >
              <el-option
                v-for="product in productOptions"
                :key="product.productId"
                :label="product.productName + ' (' + product.sku + ')'"
                :value="product.productId"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="SKU" prop="sku" width="140" />
        <el-table-column label="商品名称" prop="productName" min-width="180" />
        <el-table-column label="单价" width="120">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.unitPrice" :min="0" :precision="2" :step="1" controls-position="right" style="width: 100%" disabled />
          </template>
        </el-table-column>
        <el-table-column label="数量" width="120">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.quantity" :min="1" :step="1" controls-position="right" style="width: 100%" @change="calcLineAmount(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column label="行金额" width="140">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.lineAmount" :min="0" :precision="2" :step="1" controls-position="right" style="width: 100%" disabled />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-delete" @click="removeItem(scope.$index)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 10px;">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addItem">新增明细</el-button>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="订单详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ detail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="买家">{{ detail.buyerName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.buyerPhone }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <span v-if="detail.status === 1">已支付</span>
          <span v-else-if="detail.status === 2">已取消</span>
          <span v-else>待支付</span>
        </el-descriptions-item>
        <el-descriptions-item label="总金额">{{ detail.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detail.address }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ detail.remark }}</el-descriptions-item>
      </el-descriptions>
      <el-divider>明细</el-divider>
      <el-table :data="detail.items" border>
        <el-table-column label="SKU" prop="sku" width="140" />
        <el-table-column label="商品名称" prop="productName" min-width="200" />
        <el-table-column label="单价" prop="unitPrice" width="120" />
        <el-table-column label="数量" prop="quantity" width="100" />
        <el-table-column label="行金额" prop="lineAmount" width="120" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, addOrder, updateOrder, delOrder } from "@/api/mall/order"
import { listProduct } from "@/api/mall/product"

export default {
  name: "MallOrder",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      orderList: [],
      dateRange: [],
      title: "",
      open: false,
      detailOpen: false,
      detail: {
        items: []
      },
      statusOptions: [
        { label: "待支付", value: 0 },
        { label: "已支付", value: 1 },
        { label: "已取消", value: 2 }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: undefined,
        buyerName: undefined,
        status: undefined
      },
      form: {},
      items: [],
      productOptions: [],
      rules: {
        orderNo: [
          { required: true, message: "订单号不能为空", trigger: "blur" }
        ],
        buyerName: [
          { required: true, message: "买家名称不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getProducts()
  },
  methods: {
    getList() {
      this.loading = true
      listOrder(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.orderList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getProducts() {
      listProduct({ pageNum: 1, pageSize: 1000, status: 1 }).then(response => {
        this.productOptions = response.rows || []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        orderId: undefined,
        orderNo: undefined,
        buyerName: undefined,
        buyerPhone: undefined,
        address: undefined,
        status: 0,
        remark: undefined
      }
      this.items = []
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "新增订单"
      if (this.items.length === 0) {
        this.addItem()
      }
    },
    handleUpdate(row) {
      this.reset()
      const orderId = row.orderId || this.ids
      getOrder(orderId).then(response => {
        this.form = response.data || {}
        this.items = (response.data && response.data.items) ? response.data.items : []
        this.items.forEach(item => {
          this.calcLineAmount(item)
        })
        this.open = true
        this.title = "修改订单"
      })
    },
    handleDetail(row) {
      const orderId = row.orderId
      getOrder(orderId).then(response => {
        this.detail = response.data || { items: [] }
        this.detailOpen = true
      })
    },
    submitForm() {
      if (this.items.length === 0) {
        this.$modal.msgError("请至少添加一条明细")
        return
      }
      this.$refs["form"].validate(valid => {
        if (valid) {
          const payload = Object.assign({}, this.form, { items: this.items })
          if (this.form.orderId != undefined) {
            updateOrder(payload).then(() => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addOrder(payload).then(() => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const orderIds = row.orderId || this.ids
      this.$modal.confirm('是否确认删除订单编号为"' + orderIds + '"的数据项？').then(function() {
        return delOrder(orderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    addItem() {
      this.items.push({
        productId: undefined,
        sku: "",
        productName: "",
        unitPrice: 0,
        quantity: 1,
        lineAmount: 0
      })
    },
    removeItem(index) {
      this.items.splice(index, 1)
    },
    onProductChange(row) {
      const product = this.productOptions.find(item => item.productId === row.productId)
      if (product) {
        row.sku = product.sku
        row.productName = product.productName
        row.unitPrice = Number(product.price)
        this.calcLineAmount(row)
      }
    },
    calcLineAmount(row) {
      const price = Number(row.unitPrice || 0)
      const qty = Number(row.quantity || 0)
      row.lineAmount = Number((price * qty).toFixed(2))
    }
  }
}
</script>
