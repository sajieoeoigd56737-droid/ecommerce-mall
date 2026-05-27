# 第 2 部分：数据库设计与初始化脚本提示词

```text
请继续完成 ecommerce-mall 项目的数据库设计。

数据库名称：

ecommerce_mall

数据库初始化文件路径：

scripts/init.sql

请在 scripts/init.sql 中写完整 SQL，包括：

1. 如果数据库不存在，则创建数据库 ecommerce_mall。
2. 使用 ecommerce_mall 数据库。
3. 删除旧表，避免重复执行报错。
4. 创建以下 6 张表：
   - user 用户表
   - admin 管理员表
   - product 商品表
   - cart 购物车表
   - orders 订单表
   - order_item 订单详情表
5. 插入初始化测试数据。

数据库表要求如下。

一、user 表

字段：
- id BIGINT 主键自增
- username VARCHAR(50) 用户名，唯一，不能为空
- password VARCHAR(100) 密码，不能为空
- nickname VARCHAR(50) 昵称
- create_time DATETIME 创建时间，默认当前时间

二、admin 表

字段：
- id BIGINT 主键自增
- username VARCHAR(50) 管理员账号，唯一，不能为空
- password VARCHAR(100) 密码，不能为空
- create_time DATETIME 创建时间，默认当前时间

三、product 表

字段：
- id BIGINT 主键自增
- name VARCHAR(100) 商品名称，不能为空
- category VARCHAR(50) 商品分类
- price DECIMAL(10,2) 商品价格，不能为空
- stock INT 商品库存，默认 0
- image_url VARCHAR(255) 商品图片地址
- description TEXT 商品描述
- status TINYINT 商品状态，1 上架，0 下架，默认 1
- create_time DATETIME 创建时间，默认当前时间

四、cart 表

字段：
- id BIGINT 主键自增
- user_id BIGINT 用户 ID，不能为空
- product_id BIGINT 商品 ID，不能为空
- quantity INT 商品数量，默认 1
- create_time DATETIME 创建时间，默认当前时间

五、orders 表

字段：
- id BIGINT 主键自增
- order_no VARCHAR(50) 订单编号，不能为空
- user_id BIGINT 用户 ID，不能为空
- total_amount DECIMAL(10,2) 订单总金额，不能为空
- receiver_name VARCHAR(50) 收货人姓名
- receiver_phone VARCHAR(20) 收货人手机号
- receiver_address VARCHAR(255) 收货地址
- status TINYINT 订单状态，默认 0
  - 0 待支付
  - 1 已支付
  - 2 已发货
  - 3 已完成
  - 4 已取消
- create_time DATETIME 创建时间，默认当前时间

六、order_item 表

字段：
- id BIGINT 主键自增
- order_id BIGINT 订单 ID，不能为空
- product_id BIGINT 商品 ID，不能为空
- product_name VARCHAR(100) 商品名称
- product_price DECIMAL(10,2) 商品单价
- quantity INT 商品数量
- subtotal DECIMAL(10,2) 小计金额

初始化数据要求：

默认用户：
- username: user
- password: 123456
- nickname: 测试用户

默认管理员：
- username: admin
- password: 123456

默认商品至少 5 个：

1. 无线蓝牙耳机
   分类：数码配件
   价格：199.00
   库存：58

2. 不锈钢保温杯
   分类：家居日用
   价格：89.00
   库存：120

3. 北欧简约台灯
   分类：家居照明
   价格：129.00
   库存：36

4. 纯棉四件套
   分类：家居纺织
   价格：259.00
   库存：15

5. 智能手环
   分类：智能设备
   价格：159.00
   库存：42

图片地址可以使用在线占位图片地址，例如：
https://picsum.photos/300/300?1

要求：
1. SQL 可以重复执行。
2. 表名使用下划线风格。
3. 字段名使用下划线风格。
4. 订单表必须叫 orders，不要叫 order，因为 order 是 SQL 关键字。
5. 请同时更新 docs/database.md，说明数据库名称、表结构、初始化账号。
```
