-- 商品表：mall_product
CREATE TABLE `mall_product` (
  `product_id`   BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `sku`          VARCHAR(64)  NOT NULL COMMENT 'SKU(唯一)',
  `product_name` VARCHAR(128) NOT NULL COMMENT '商品名称',
  `category`     VARCHAR(64)  DEFAULT NULL COMMENT '类目',
  `price`        DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '单价',
  `stock`        INT NOT NULL DEFAULT 0 COMMENT '库存',
  `status`       TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0下架,1上架)',
  `remark`       VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `create_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag`     CHAR(1) NOT NULL DEFAULT '0' COMMENT '删除标志(0存在,2删除)',
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `uk_mall_product_sku` (`sku`),
  KEY `idx_mall_product_name` (`product_name`),
  KEY `idx_mall_product_status` (`status`),
  KEY `idx_mall_product_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城-商品表';

-- 订单主表：mall_order
CREATE TABLE `mall_order` (
  `order_id`     BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no`     VARCHAR(64) NOT NULL COMMENT '订单号(唯一)',
  `buyer_name`   VARCHAR(64) NOT NULL COMMENT '买家名称',
  `buyer_phone`  VARCHAR(32) DEFAULT NULL COMMENT '买家手机号',
  `address`      VARCHAR(256) DEFAULT NULL COMMENT '收货地址',
  `status`       TINYINT NOT NULL DEFAULT 0 COMMENT '状态(0待支付,1已支付,2已取消)',
  `total_amount` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '订单总金额',
  `remark`       VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `create_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `uk_mall_order_no` (`order_no`),
  KEY `idx_mall_order_status` (`status`),
  KEY `idx_mall_order_buyer_name` (`buyer_name`),
  KEY `idx_mall_order_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城-订单主表';

-- 订单明细表：mall_order_item
CREATE TABLE `mall_order_item` (
  `item_id`      BIGINT NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id`     BIGINT NOT NULL COMMENT '订单ID',
  `product_id`   BIGINT NOT NULL COMMENT '商品ID',
  `sku`          VARCHAR(64)  NOT NULL COMMENT 'SKU冗余',
  `product_name` VARCHAR(128) NOT NULL COMMENT '商品名称冗余',
  `unit_price`   DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '下单单价',
  `quantity`     INT NOT NULL DEFAULT 1 COMMENT '数量',
  `line_amount`  DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '行金额',
  PRIMARY KEY (`item_id`),
  KEY `idx_mall_order_item_order_id` (`order_id`),
  KEY `idx_mall_order_item_product_id` (`product_id`),
  CONSTRAINT `fk_mall_order_item_order`
    FOREIGN KEY (`order_id`) REFERENCES `mall_order` (`order_id`)
    ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_mall_order_item_product`
    FOREIGN KEY (`product_id`) REFERENCES `mall_product` (`product_id`)
    ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商城-订单明细表';
