-- 菜单 SQL
-- ----------------------------
-- 1. 商城管理目录
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2000, '商城管理', 0, 10, 'mall', NULL, 1, 0, 'M', '0', '0', '', 'shopping', 'admin', sysdate(), '', NULL, '商城管理目录');

-- ----------------------------
-- 2. 商品管理菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2001, '商品管理', 2000, 1, 'product', 'mall/product/index', 1, 0, 'C', '0', '0', 'mall:product:list', 'shangpin', 'admin', sysdate(), '', NULL, '商品管理菜单');

-- 按钮-查询
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2002, '商品查询', 2001, 1, '#', '', 1, 0, 'F', '0', '0', 'mall:product:query', '#', 'admin', sysdate(), '', NULL, '');

-- 按钮-新增
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2003, '商品新增', 2001, 2, '#', '', 1, 0, 'F', '0', '0', 'mall:product:add', '#', 'admin', sysdate(), '', NULL, '');

-- 按钮-修改
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2004, '商品修改', 2001, 3, '#', '', 1, 0, 'F', '0', '0', 'mall:product:edit', '#', 'admin', sysdate(), '', NULL, '');

-- 按钮-删除
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2005, '商品删除', 2001, 4, '#', '', 1, 0, 'F', '0', '0', 'mall:product:remove', '#', 'admin', sysdate(), '', NULL, '');

-- 按钮-导出
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2006, '商品导出', 2001, 5, '#', '', 1, 0, 'F', '0', '0', 'mall:product:export', '#', 'admin', sysdate(), '', NULL, '');


-- ----------------------------
-- 3. 订单管理菜单
-- ----------------------------
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2010, '订单管理', 2000, 2, 'order', 'mall/order/index', 1, 0, 'C', '0', '0', 'mall:order:list', 'shopping-cart', 'admin', sysdate(), '', NULL, '订单管理菜单');

-- 按钮-查询
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2011, '订单查询', 2010, 1, '#', '', 1, 0, 'F', '0', '0', 'mall:order:query', '#', 'admin', sysdate(), '', NULL, '');

-- 按钮-新增
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2012, '订单新增', 2010, 2, '#', '', 1, 0, 'F', '0', '0', 'mall:order:add', '#', 'admin', sysdate(), '', NULL, '');

-- 按钮-修改
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2013, '订单修改', 2010, 3, '#', '', 1, 0, 'F', '0', '0', 'mall:order:edit', '#', 'admin', sysdate(), '', NULL, '');

-- 按钮-删除
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2014, '订单删除', 2010, 4, '#', '', 1, 0, 'F', '0', '0', 'mall:order:remove', '#', 'admin', sysdate(), '', NULL, '');

-- 按钮-导出
INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES (2015, '订单导出', 2010, 5, '#', '', 1, 0, 'F', '0', '0', 'mall:order:export', '#', 'admin', sysdate(), '', NULL, '');
