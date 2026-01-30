<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="买家名称" prop="buyerName">
        <el-input
          v-model="queryParams.buyerName"
          placeholder="请输入买家名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option label="待支付" value="0" />
          <el-option label="已支付" value="1" />
          <el-option label="已取消" value="2" />
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
        ></el-date-picker>
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
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单ID" align="center" prop="orderId" width="100" />
      <el-table-column label="订单号" align="center" prop="orderNo" width="150" />
      <el-table-column label="买家名称" align="center" prop="buyerName" width="120" />
      <el-table-column label="买家手机号" align="center" prop="buyerPhone" width="120" />
      <el-table-column label="订单总金额" align="center" prop="totalAmount" width="120">
        <template slot-scope="scope">
          {{ scope.row.totalAmount ? scope.row.totalAmount.toFixed(2) : '0.00' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
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

    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="请输入订单号" :disabled="form.orderId != null" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态">
                <el-option label="待支付" :value="0" />
                <el-option label="已支付" :value="1" />
                <el-option label="已取消" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="买家名称" prop="buyerName">
              <el-input v-model="form.buyerName" placeholder="请输入买家名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买家手机号" prop="buyerPhone">
              <el-input v-model="form.buyerPhone" placeholder="请输入买家手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="收货地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="订单明细">
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="addItem">添加明细</el-button>
        </el-form-item>
        <el-table :data="form.items" border style="width: 100%">
          <el-table-column label="商品" width="200">
            <template slot-scope="scope">
              <el-select v-model="scope.row.productId" placeholder="请选择商品" @change="handleProductChange(scope.row)" filterable>
                <el-option
                  v-for="item in productList"
                  :key="item.productId"
                  :label="item.productName"
                  :value="item.productId">
                  <span>{{ item.productName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.price }}</span>
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="SKU" width="120">
            <template slot-scope="scope">
              {{ scope.row.sku }}
            </template>
          </el-table-column>
          <el-table-column label="单价" width="100">
            <template slot-scope="scope">
              {{ scope.row.unitPrice ? scope.row.unitPrice.toFixed(2) : '0.00' }}
            </template>
          </el-table-column>
          <el-table-column label="数量" width="120">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" :min="1" :step="1" size="mini" @change="calculateLineAmount(scope.row)" />
            </template>
          </el-table-column>
          <el-table-column label="行金额" width="120">
            <template slot-scope="scope">
              {{ scope.row.lineAmount ? scope.row.lineAmount.toFixed(2) : '0.00' }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button type="text" size="mini" icon="el-icon-delete" @click="deleteItem(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-form-item label="订单总金额">
          <span style="font-size: 18px; font-weight: bold; color: #F56C6C;">
            ¥{{ calculateTotalAmount().toFixed(2) }}
          </span>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="订单详情" :visible.sync="detailOpen" width="900px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ orderDetail.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(orderDetail.status)">
            {{ getStatusText(orderDetail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="买家名称">{{ orderDetail.buyerName }}</el-descriptions-item>
        <el-descriptions-item label="买家手机号">{{ orderDetail.buyerPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ orderDetail.address }}</el-descriptions-item>
        <el-descriptions-item label="订单总金额" :span="2">
          <span style="font-size: 18px; font-weight: bold; color: #F56C6C;">
            ¥{{ orderDetail.totalAmount ? orderDetail.totalAmount.toFixed(2) : '0.00' }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(orderDetail.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ orderDetail.remark }}</el-descriptions-item>
      </el-descriptions>
      <el-divider content-position="left">订单明细</el-divider>
      <el-table :data="orderDetail.items" border>
        <el-table-column label="商品名称" prop="productName" />
        <el-table-column label="SKU" prop="sku" width="120" />
        <el-table-column label="单价" width="100">
          <template slot-scope="scope">
            {{ scope.row.unitPrice ? scope.row.unitPrice.toFixed(2) : '0.00' }}
          </template>
        </el-table-column>
        <el-table-column label="数量" prop="quantity" width="80" />
        <el-table-column label="行金额" width="120">
          <template slot-scope="scope">
            {{ scope.row.lineAmount ? scope.row.lineAmount.toFixed(2) : '0.00' }}
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, updateOrder } from "@/api/mall/order";
import { listProduct } from "@/api/mall/product";

export default {
  name: "Order",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      orderList: [],
      productList: [],
      title: "",
      open: false,
      detailOpen: false,
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        buyerName: null,
        status: null
      },
      form: {
        items: []
      },
      orderDetail: {
        items: []
      },
      rules: {
        orderNo: [
          { required: true, message: "订单号不能为空", trigger: "blur" }
        ],
        buyerName: [
          { required: true, message: "买家名称不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getProductList();
  },
  methods: {
    getList() {
      this.loading = true;
      listOrder(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getProductList() {
      listProduct({ pageNum: 1, pageSize: 1000 }).then(response => {
        this.productList = response.rows;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        orderId: null,
        orderNo: null,
        buyerName: null,
        buyerPhone: null,
        address: null,
        status: 0,
        remark: null,
        items: []
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订单";
    },
    handleUpdate(row) {
      this.reset();
      const orderId = row.orderId || this.ids[0];
      getOrder(orderId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改订单";
      });
    },
    handleDetail(row) {
      const orderId = row.orderId;
      getOrder(orderId).then(response => {
        this.orderDetail = response.data;
        this.detailOpen = true;
      });
    },
    addItem() {
      this.form.items.push({
        productId: null,
        sku: '',
        productName: '',
        unitPrice: 0,
        quantity: 1,
        lineAmount: 0
      });
    },
    deleteItem(index) {
      this.form.items.splice(index, 1);
    },
    handleProductChange(row) {
      const product = this.productList.find(p => p.productId === row.productId);
      if (product) {
        row.sku = product.sku;
        row.productName = product.productName;
        row.unitPrice = product.price;
        this.calculateLineAmount(row);
      }
    },
    calculateLineAmount(row) {
      if (row.unitPrice && row.quantity) {
        row.lineAmount = row.unitPrice * row.quantity;
      } else {
        row.lineAmount = 0;
      }
    },
    calculateTotalAmount() {
      let total = 0;
      if (this.form.items && this.form.items.length > 0) {
        this.form.items.forEach(item => {
          if (item.lineAmount) {
            total += item.lineAmount;
          }
        });
      }
      return total;
    },
    getStatusType(status) {
      if (status === 0) return 'warning';
      if (status === 1) return 'success';
      if (status === 2) return 'info';
      return '';
    },
    getStatusText(status) {
      if (status === 0) return '待支付';
      if (status === 1) return '已支付';
      if (status === 2) return '已取消';
      return '';
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.items.length === 0) {
            this.$modal.msgError("订单明细不能为空");
            return;
          }
          if (this.form.orderId != null) {
            updateOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addOrder(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const orderIds = row.orderId || this.ids;
      this.$modal.confirm('是否确认删除订单编号为"' + orderIds + '"的数据项？').then(function() {
        return delOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
