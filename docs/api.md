# 接口文档

## 基本约定

- 服务地址：`http://localhost:8080`
- 接口前缀：`/api`
- 请求和响应格式：`application/json`
- 用户和管理员登录成功后，由前端在 `localStorage` 中保存返回的基本信息。
- 当前为课程 MVP，测试账号密码按初始化数据明文校验。

统一成功响应：

```json
{
  "code": 200,
  "message": "成功",
  "data": {}
}
```

统一失败响应：

```json
{
  "code": 500,
  "message": "错误原因",
  "data": null
}
```

## 用户接口

### 注册

- 请求：`POST /api/user/register`

```json
{
  "username": "new_user",
  "password": "123456",
  "nickname": "新用户"
}
```

成功响应中的 `data` 为 `null`。用户名或密码为空、用户名已存在时返回失败响应。

### 登录

- 请求：`POST /api/user/login`

```json
{
  "username": "user",
  "password": "123456"
}
```

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "username": "user",
    "nickname": "测试用户"
  }
}
```

### 获取用户信息

- 请求：`GET /api/user/info/{id}`
- 路径参数：`id` 为用户 ID。

响应 `data` 字段格式与登录成功响应相同。

## 商品接口

### 商品列表

- 请求：`GET /api/product/list`
- 说明：仅返回 `status = 1` 的上架商品。

```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "id": 1,
      "name": "无线蓝牙耳机",
      "category": "数码配件",
      "price": 199.00,
      "stock": 58,
      "imageUrl": "https://picsum.photos/300/300?1",
      "description": "轻便易携带的无线蓝牙耳机。",
      "status": 1,
      "createTime": "2026-05-26T16:00:00"
    }
  ]
}
```

### 商品详情

- 请求：`GET /api/product/{id}`
- 路径参数：`id` 为商品 ID。

响应 `data` 为一条商品对象；商品不存在时返回失败响应。

## 购物车接口

### 查询购物车

- 请求：`GET /api/cart/list/{userId}`
- 路径参数：`userId` 为用户 ID。

```json
{
  "code": 200,
  "message": "成功",
  "data": [
    {
      "cartId": 1,
      "productId": 1,
      "productName": "无线蓝牙耳机",
      "productImage": "https://picsum.photos/300/300?1",
      "price": 199.00,
      "quantity": 2,
      "subtotal": 398.00
    }
  ]
}
```

### 加入购物车

- 请求：`POST /api/cart/add`

```json
{
  "userId": 1,
  "productId": 1,
  "quantity": 1
}
```

后端校验用户、商品、上架状态及库存；相同商品已存在时累加数量。

### 修改商品数量

- 请求：`PUT /api/cart/update`

```json
{
  "cartId": 1,
  "quantity": 2
}
```

`quantity` 必须大于 `0`，且不能超过商品库存。

### 删除购物车商品

- 请求：`DELETE /api/cart/delete/{cartId}`
- 路径参数：`cartId` 为购物车记录 ID。

## 订单接口

### 创建订单

- 请求：`POST /api/order/create`

```json
{
  "userId": 1,
  "receiverName": "张三",
  "receiverPhone": "13800000000",
  "receiverAddress": "广东省广州市天河区"
}
```

后端从购物车和商品表读取真实单价并计算总金额，在同一事务中保存订单及明细、扣减库存并清空购物车。

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "orderNo": "ORD202605261600001231234",
    "userId": 1,
    "totalAmount": 199.00,
    "receiverName": "张三",
    "receiverPhone": "13800000000",
    "receiverAddress": "广东省广州市天河区",
    "status": 0,
    "createTime": "2026-05-26T16:00:00",
    "items": [
      {
        "id": 1,
        "orderId": 1,
        "productId": 1,
        "productName": "无线蓝牙耳机",
        "productPrice": 199.00,
        "quantity": 1,
        "subtotal": 199.00
      }
    ]
  }
}
```

### 我的订单

- 请求：`GET /api/order/list/{userId}`
- 路径参数：`userId` 为用户 ID。
- 说明：按创建时间倒序返回订单及其商品明细。

### 模拟支付

- 请求：`PUT /api/order/pay/{orderId}`
- 路径参数：`orderId` 为订单 ID。
- 说明：仅待支付订单可以由状态 `0` 更新为 `1`。

## 管理员接口

### 管理员登录

- 请求：`POST /api/admin/login`

```json
{
  "username": "admin",
  "password": "123456"
}
```

```json
{
  "code": 200,
  "message": "成功",
  "data": {
    "id": 1,
    "username": "admin"
  }
}
```

### 后台商品列表

- 请求：`GET /api/admin/product/list`
- 说明：返回所有商品，包括上架和下架商品。

### 新增商品

- 请求：`POST /api/admin/product/add`

```json
{
  "name": "无线蓝牙耳机",
  "category": "数码配件",
  "price": 199.00,
  "stock": 58,
  "imageUrl": "https://picsum.photos/300/300?1",
  "description": "高音质蓝牙耳机，适合日常使用",
  "status": 1
}
```

`name`、`price`、`stock` 为必填字段。

### 修改商品

- 请求：`PUT /api/admin/product/update`
- 请求体：字段与新增商品相同，并额外包含商品 `id`。

```json
{
  "id": 1,
  "name": "无线蓝牙耳机",
  "category": "数码配件",
  "price": 189.00,
  "stock": 50,
  "imageUrl": "https://picsum.photos/300/300?1",
  "description": "修改后的商品描述",
  "status": 1
}
```

### 删除商品

- 请求：`DELETE /api/admin/product/delete/{id}`
- 路径参数：`id` 为商品 ID。

### 后台订单列表

- 请求：`GET /api/admin/order/list`
- 说明：按创建时间倒序返回全部订单及其商品明细。

### 订单发货

- 请求：`PUT /api/admin/order/ship/{orderId}`
- 路径参数：`orderId` 为订单 ID。
- 说明：仅已支付订单可以由状态 `1` 更新为 `2`。

## 字段映射与订单状态

- 数据库字段 `image_url` 映射为 JSON 字段 `imageUrl`。
- 购物车展示对象中的商品图片字段为 `productImage`。

| 订单状态值 | 含义 |
| ---: | --- |
| `0` | 待支付 |
| `1` | 已支付 |
| `2` | 已发货 |
| `3` | 已完成 |
| `4` | 已取消 |
