# 商城模块交付清单

## 1. 新增/修改文件清单

### 后端 (ruoyi-modules/ruoyi-mall)
*   **配置与启动**:
    *   `pom.xml` (新增模块依赖)
    *   `src/main/resources/bootstrap.yml` (Nacos/Sentinel配置)
    *   `src/main/java/com/ruoyi/mall/RuoYiMallApplication.java` (启动类)
*   **Domain (实体类)**:
    *   `src/main/java/com/ruoyi/mall/domain/MallProduct.java`
    *   `src/main/java/com/ruoyi/mall/domain/MallOrder.java`
    *   `src/main/java/com/ruoyi/mall/domain/MallOrderItem.java`
*   **Mapper (持久层)**:
    *   `src/main/java/com/ruoyi/mall/mapper/MallProductMapper.java`
    *   `src/main/java/com/ruoyi/mall/mapper/MallProductMapper.xml`
    *   `src/main/java/com/ruoyi/mall/mapper/MallOrderMapper.java`
    *   `src/main/java/com/ruoyi/mall/mapper/MallOrderMapper.xml`
    *   `src/main/java/com/ruoyi/mall/mapper/MallOrderItemMapper.java`
    *   `src/main/java/com/ruoyi/mall/mapper/MallOrderItemMapper.xml`
*   **Service (业务层)**:
    *   `src/main/java/com/ruoyi/mall/service/IMallProductService.java`
    *   `src/main/java/com/ruoyi/mall/service/impl/MallProductServiceImpl.java`
    *   `src/main/java/com/ruoyi/mall/service/IMallOrderService.java`
    *   `src/main/java/com/ruoyi/mall/service/impl/MallOrderServiceImpl.java`
*   **Controller (控制层)**:
    *   `src/main/java/com/ruoyi/mall/controller/MallProductController.java`
    *   `src/main/java/com/ruoyi/mall/controller/MallOrderController.java`
*   **全局配置**:
    *   `ruoyi-modules/pom.xml` (添加 ruoyi-mall 模块)

### 前端 (ruoyi-ui)
*   **API**:
    *   `src/api/mall/product.js`
    *   `src/api/mall/order.js`
*   **Views (页面)**:
    *   `src/views/mall/product/index.vue`
    *   `src/views/mall/order/index.vue`

### SQL
*   `sql/task_mall.sql` (表结构)
*   `sql/mall_menu.sql` (菜单数据 - 新增)

---

## 2. 接口清单

所有接口通过网关访问，假设网关地址 `localhost:8080`，路由前缀 `/mall`。

### 商品管理 (Product)
| 功能 | Method | URL | 示例参数 | 响应说明 |
| :--- | :--- | :--- | :--- | :--- |
| 查询列表 | GET | `/mall/product/list` | `pageNum=1&pageSize=10&sku=SKU001` | `{rows: [...], total: 10, code: 200}` |
| 获取详情 | GET | `/mall/product/{productId}` | `1` | `{data: {...}, code: 200}` |
| 新增商品 | POST | `/mall/product` | `{"sku":"A001","productName":"手机","price":100,"stock":10}` | `{code: 200, msg: "操作成功"}` |
| 修改商品 | PUT | `/mall/product` | `{"productId":1,"productName":"新手机"}` | `{code: 200, msg: "操作成功"}` |
| 删除商品 | DELETE | `/mall/product/{productIds}` | `1,2` | `{code: 200, msg: "操作成功"}` |
| 导出 | POST | `/mall/product/export` | `{}` | Excel文件流 |

### 订单管理 (Order)
| 功能 | Method | URL | 示例参数 | 响应说明 |
| :--- | :--- | :--- | :--- | :--- |
| 查询列表 | GET | `/mall/order/list` | `pageNum=1&pageSize=10&orderNo=DD001` | `{rows: [...], total: 10, code: 200}` |
| 获取详情 | GET | `/mall/order/{orderId}` | `1` | `{data: {..., mallOrderItemList: [...]}, code: 200}` |
| 新增订单 | POST | `/mall/order` | `{"buyerName":"张三","mallOrderItemList":[{"productId":1,"quantity":2}]}` | `{code: 200, msg: "操作成功"}` |
| 修改订单 | PUT | `/mall/order` | `{"orderId":1,"status":1}` | `{code: 200, msg: "操作成功"}` |
| 删除订单 | DELETE | `/mall/order/{orderIds}` | `1` | `{code: 200, msg: "操作成功"}` |
| 导出 | POST | `/mall/order/export` | `{}` | Excel文件流 |

---

## 3. 验证方式

### 步骤 1: 数据库准备
1. 创建数据库 `ry-cloud` (如果不存在)。
2. 执行 `sql/task_mall.sql` 创建业务表。
3. 执行 `sql/mall_menu.sql` 创建菜单和按钮权限。

### 步骤 2: 网关配置 (Nacos)
在 Nacos 配置中心 (`ruoyi-gateway-dev.yml`) 中添加 `ruoyi-mall` 的路由：
```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: ruoyi-mall
          uri: lb://ruoyi-mall
          predicates:
            - Path=/mall/**
          filters:
            - StripPrefix=1
```
*注：`StripPrefix=1` 必不可少，它将 `/mall/product/...` 转发为 `/product/...`，否则会报 404。*

### 步骤 3: 启动后端服务
请按顺序启动以下服务：
1. **Nacos** (必须)
2. **Redis** (必须)
3. `RuoYiGatewayApplication` (网关端口 8080)
4. `RuoYiAuthApplication` (认证中心)
5. `RuoYiSystemApplication` (系统模块)
6. **`RuoYiMallApplication`** (商城模块，端口 9301)

### 步骤 4: 启动前端
```bash
cd ruoyi-ui
npm install
npm run dev
```

### 步骤 5: 功能验证
1. 登录系统 (admin/admin123)。
2. 刷新页面，左侧菜单应出现 **"商城管理"**。
3. 进入 **"商品管理"**：
   - 新增商品：录入 SKU、名称、价格、库存。
   - 验证：SKU 重复报错；价格/库存负数报错。
4. 进入 **"订单管理"**：
   - 新增订单：点击“添加商品”按钮，选择刚才创建的商品。
   - 验证：自动回填价格，修改数量自动计算金额。
   - 保存后查看详情，验证主表金额和明细数据是否一致。
   - 验证：库存是否扣减 (需查看商品管理或数据库)。
