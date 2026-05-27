# 数据库设计说明

## 基本信息

- 数据库名称：`ecommerce_mall`
- 初始化脚本：`scripts/init.sql`
- 字符集：`utf8mb4`
- 数据库类型：MySQL

初始化脚本会在数据库不存在时创建 `ecommerce_mall`，随后按依赖顺序删除并重新创建全部业务表，再写入初始测试数据。因此脚本可以重复执行，但每次执行都会清空已有业务数据。

## 表结构

### `user` 用户表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `BIGINT` | 主键，自增 |
| `username` | `VARCHAR(50)` | 用户名，非空且唯一 |
| `password` | `VARCHAR(100)` | 密码，非空 |
| `nickname` | `VARCHAR(50)` | 昵称 |
| `create_time` | `DATETIME` | 创建时间，默认当前时间 |

### `admin` 管理员表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `BIGINT` | 主键，自增 |
| `username` | `VARCHAR(50)` | 管理员账号，非空且唯一 |
| `password` | `VARCHAR(100)` | 密码，非空 |
| `create_time` | `DATETIME` | 创建时间，默认当前时间 |

### `product` 商品表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `BIGINT` | 主键，自增 |
| `name` | `VARCHAR(100)` | 商品名称，非空 |
| `category` | `VARCHAR(50)` | 商品分类 |
| `price` | `DECIMAL(10,2)` | 商品价格，非空 |
| `stock` | `INT` | 商品库存，默认 `0` |
| `image_url` | `VARCHAR(255)` | 商品图片 URL |
| `description` | `TEXT` | 商品描述 |
| `status` | `TINYINT` | `1` 上架，`0` 下架，默认 `1` |
| `create_time` | `DATETIME` | 创建时间，默认当前时间 |

### `cart` 购物车表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `BIGINT` | 主键，自增 |
| `user_id` | `BIGINT` | 用户 ID，非空 |
| `product_id` | `BIGINT` | 商品 ID，非空 |
| `quantity` | `INT` | 商品数量，默认 `1` |
| `create_time` | `DATETIME` | 创建时间，默认当前时间 |

### `orders` 订单表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `BIGINT` | 主键，自增 |
| `order_no` | `VARCHAR(50)` | 订单编号，非空且唯一 |
| `user_id` | `BIGINT` | 用户 ID，非空 |
| `total_amount` | `DECIMAL(10,2)` | 订单总金额，非空 |
| `receiver_name` | `VARCHAR(50)` | 收货人姓名 |
| `receiver_phone` | `VARCHAR(20)` | 收货人手机号 |
| `receiver_address` | `VARCHAR(255)` | 收货地址 |
| `status` | `TINYINT` | 订单状态，默认 `0` |
| `create_time` | `DATETIME` | 创建时间，默认当前时间 |

订单状态值如下：

| 值 | 状态 |
| --- | --- |
| `0` | 待支付 |
| `1` | 已支付 |
| `2` | 已发货 |
| `3` | 已完成 |
| `4` | 已取消 |

### `order_item` 订单详情表

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `BIGINT` | 主键，自增 |
| `order_id` | `BIGINT` | 订单 ID，非空 |
| `product_id` | `BIGINT` | 商品 ID，非空 |
| `product_name` | `VARCHAR(100)` | 下单时的商品名称 |
| `product_price` | `DECIMAL(10,2)` | 下单时的商品单价 |
| `quantity` | `INT` | 商品数量 |
| `subtotal` | `DECIMAL(10,2)` | 小计金额 |

## 初始化账号

| 角色 | 账号 | 密码 | 说明 |
| --- | --- | --- | --- |
| 普通用户 | `user` | `123456` | 昵称为“测试用户” |
| 管理员 | `admin` | `123456` | 用于管理端登录 |

初始密码为课程 MVP 的测试数据，后续仅用于本地演示环境。

## 初始商品

| 商品名称 | 分类 | 价格 | 库存 |
| --- | --- | ---: | ---: |
| 无线蓝牙耳机 | 数码配件 | 199.00 | 58 |
| 不锈钢保温杯 | 家居日用 | 89.00 | 120 |
| 北欧简约台灯 | 家居照明 | 129.00 | 36 |
| 纯棉四件套 | 家居纺织 | 259.00 | 15 |
| 智能手环 | 智能设备 | 159.00 | 42 |

商品图片使用在线占位图片 URL，通过 `image_url` 字段直接保存。
