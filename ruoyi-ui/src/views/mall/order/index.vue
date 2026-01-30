<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="买家名称" prop="buyerName">
        <el-input
          v-model="queryParams.buyerName"
          placeholder="请输入买家名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="待支付" :value="0" />
          <el-option label="已支付" :value="1" />
          <el-option label="已取消" :value="2" />
        </el-select>
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
      <el-table-column label="订单ID" align="center" prop="orderId" />
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="买家名称" align="center" prop="buyerName" />
      <el-table-column label="买家手机" align="center" prop="buyerPhone" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0">待支付</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="success">已支付</el-tag>
          <el-tag v-else type="info">已取消</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="总金额" align="center" prop="totalAmount" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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

    <!-- 添加或修改订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="订单号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="请输入订单号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买家名称" prop="buyerName">
              <el-input v-model="form.buyerName" placeholder="请输入买家名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="买家手机" prop="buyerPhone">
              <el-input v-model="form.buyerPhone" placeholder="请输入买家手机" />
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
          <el-col :span="24">
            <el-form-item label="收货地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入收货地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-divider content-position="center">订单明细</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddProduct">添加商品</el-button>
          </el-col>
        </el-row>
        <el-table :data="mallOrderItemList" :row-class-name="rowClassName" @selection-change="handleSelectionChange">
          <el-table-column label="序号" align="center" type="index" width="50"/>
          <el-table-column label="商品名称" prop="productName" width="150" />
          <el-table-column label="SKU" prop="sku" width="150" />
          <el-table-column label="单价" prop="unitPrice" width="150" />
          <el-table-column label="数量" prop="quantity" width="150">
            <template slot-scope="scope">
              <el-input-number v-model="scope.row.quantity" :min="1" :step="1" size="mini" @change="calculateLineAmount(scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column label="行金额" prop="lineAmount" width="150" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDeleteProduct(scope.$index, scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align: right; margin-top: 10px;">
           总金额: <span style="color: red; font-weight: bold;">{{ form.totalAmount }}</span>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 选择商品对话框 -->
    <el-dialog title="选择商品" :visible.sync="productOpen" width="800px" append-to-body>
        <el-form :model="productQueryParams" ref="productQueryForm" size="small" :inline="true">
            <el-form-item label="商品名称" prop="productName">
                <el-input v-model="productQueryParams.productName" placeholder="请输入商品名称" clearable/>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="getProductList">搜索</el-button>
            </el-form-item>
        </el-form>
        <el-table v-loading="productLoading" :data="productSelectionList" @row-click="handleSelectProduct">
            <el-table-column label="SKU" align="center" prop="sku" />
            <el-table-column label="商品名称" align="center" prop="productName" />
            <el-table-column label="价格" align="center" prop="price" />
            <el-table-column label="库存" align="center" prop="stock" />
        </el-table>
        <pagination
            v-show="productTotal>0"
            :total="productTotal"
            :page.sync="productQueryParams.pageNum"
            :limit.sync="productQueryParams.pageSize"
            @pagination="getProductList"
        />
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="订单详情" :visible.sync="detailOpen" width="800px" append-to-body>
        <el-descriptions title="基本信息">
            <el-descriptions-item label="订单号">{{ detailForm.orderNo }}</el-descriptions-item>
            <el-descriptions-item label="买家名称">{{ detailForm.buyerName }}</el-descriptions-item>
            <el-descriptions-item label="买家手机">{{ detailForm.buyerPhone }}</el-descriptions-item>
            <el-descriptions-item label="状态">
                <el-tag v-if="detailForm.status === 0">待支付</el-tag>
                <el-tag v-else-if="detailForm.status === 1" type="success">已支付</el-tag>
                <el-tag v-else type="info">已取消</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="总金额">{{ detailForm.totalAmount }}</el-descriptions-item>
            <el-descriptions-item label="收货地址">{{ detailForm.address }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="center">订单明细</el-divider>
        <el-table :data="detailForm.mallOrderItemList">
            <el-table-column label="商品名称" prop="productName" />
            <el-table-column label="SKU" prop="sku" />
            <el-table-column label="单价" prop="unitPrice" />
            <el-table-column label="数量" prop="quantity" />
            <el-table-column label="行金额" prop="lineAmount" />
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
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 订单表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      detailOpen: false,
      detailForm: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        buyerName: null,
        status: null,
      },
      // 表单参数
      form: {
          totalAmount: 0
      },
      // 订单明细
      mallOrderItemList: [],
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "订单号不能为空", trigger: "blur" }
        ],
        buyerName: [
          { required: true, message: "买家名称不能为空", trigger: "blur" }
        ],
      },
      // 商品选择
      productOpen: false,
      productLoading: false,
      productSelectionList: [],
      productTotal: 0,
      productQueryParams: {
          pageNum: 1,
          pageSize: 10,
          productName: null,
          status: 1 // 只查上架
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询订单列表 */
    getList() {
      this.loading = true;
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        orderId: null,
        orderNo: null,
        buyerName: null,
        buyerPhone: null,
        address: null,
        status: 0,
        totalAmount: 0,
        remark: null
      };
      this.mallOrderItemList = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加订单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const orderId = row.orderId || this.ids
      getOrder(orderId).then(response => {
        this.form = response.data;
        this.mallOrderItemList = response.data.mallOrderItemList;
        this.calculateTotalAmount(); // Ensure total is correct
        this.open = true;
        this.title = "修改订单";
      });
    },
    /** 详情按钮 */
    handleDetail(row) {
        getOrder(row.orderId).then(response => {
            this.detailForm = response.data;
            this.detailOpen = true;
        });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.mallOrderItemList = this.mallOrderItemList;
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const orderIds = row.orderId || this.ids;
      this.$modal.confirm('是否确认删除订单编号为"' + orderIds + '"的数据项？').then(function() {
        return delOrder(orderIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 增加明细 */
    handleAddProduct() {
        this.productOpen = true;
        this.getProductList();
    },
    /** 删除明细 */
    handleDeleteProduct(index, row) {
        this.mallOrderItemList.splice(index, 1);
        this.calculateTotalAmount();
    },
    /** 获取商品列表 */
    getProductList() {
        this.productLoading = true;
        listProduct(this.productQueryParams).then(response => {
            this.productSelectionList = response.rows;
            this.productTotal = response.total;
            this.productLoading = false;
        });
    },
    /** 选择商品 */
    handleSelectProduct(row) {
        const item = {
           productId: row.productId,
           sku: row.sku,
           productName: row.productName,
           unitPrice: row.price,
           quantity: 1,
           lineAmount: row.price
        };
        // Check if exists? Maybe allow duplicates or merge? 
        // For simplicity, just add.
        this.mallOrderItemList.push(item);
        this.calculateTotalAmount();
        this.productOpen = false;
        this.$modal.msgSuccess("已添加 " + row.productName);
    },
    /** 计算行金额 */
    calculateLineAmount(row) {
        row.lineAmount = (row.unitPrice * row.quantity).toFixed(2);
        this.calculateTotalAmount();
    },
    /** 计算总金额 */
    calculateTotalAmount() {
        let total = 0;
        this.mallOrderItemList.forEach(item => {
            total += parseFloat(item.lineAmount);
        });
        this.form.totalAmount = total.toFixed(2);
    },
    rowClassName({ row, rowIndex }) {
      row.index = rowIndex + 1;
    }
  }
};
</script>
