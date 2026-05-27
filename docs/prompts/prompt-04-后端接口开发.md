# 第 4 部分：后端接口开发提示词

```text
请继续完成 ecommerce-mall/backend 的后端接口开发。

要求：

1. 所有接口统一以 /api 开头。
2. 所有返回结果必须使用 Result 包装。
3. 不要返回异常堆栈给前端。
4. 业务错误返回 Result.error("错误原因")。
5. 密码可以明文存储，因为这是课程作业 MVP。
6. 不要使用复杂 JWT 权限认证。
7. 用户登录信息由前端 localStorage 保存即可。
8. 后端必须真实连接 MySQL 数据库。
9. 不要使用 mock 数据。

需要完成以下接口。

==================================================
一、用户接口 UserController
==================================================

1. 用户注册

POST /api/user/register

请求参数：

{
  "username": "user",
  "password": "123456",
  "nickname": "测试用户"
}

逻辑：

- username 不能为空
- password 不能为空
- 查询 username 是否已经存在
- 如果存在，返回"用户名已存在"
- 如果不存在，写入 user 表
- 返回注册成功

2. 用户登录

POST /api/user/login

请求参数：

{
  "username": "user",
  "password": "123456"
}

逻辑：

- 根据 username 查询用户
- 用户不存在，返回"用户不存在"
- 密码错误，返回"密码错误"
- 登录成功，返回用户基本信息：
  - id
  - username
  - nickname

3. 获取用户信息

GET /api/user/info/{id}

逻辑：

- 根据 id 查询用户
- 用户不存在返回错误
- 用户存在返回用户信息

==================================================
二、商品接口 ProductController
==================================================

1. 用户端商品列表

GET /api/product/list

逻辑：

- 查询 product 表中 status = 1 的商品
- 返回商品列表

2. 商品详情

GET /api/product/{id}

逻辑：

- 根据 id 查询商品
- 商品不存在返回"商品不存在"
- 商品存在返回商品详情

==================================================
三、购物车接口 CartController
==================================================

1. 查询购物车

GET /api/cart/list/{userId}

逻辑：

- 根据 userId 查询购物车
- 关联 product 表返回商品信息
- 返回字段包括：
  - cartId
  - productId
  - productName
  - productImage
  - price
  - quantity
  - subtotal

CartVO 字段建议：

- Long cartId
- Long productId
- String productName
- String productImage
- BigDecimal price
- Integer quantity
- BigDecimal subtotal

2. 加入购物车

POST /api/cart/add

请求参数：

{
  "userId": 1,
  "productId": 1,
  "quantity": 1
}

逻辑：

- 判断用户是否存在
- 判断商品是否存在
- 判断商品是否上架
- 判断库存是否足够
- 判断当前用户购物车中是否已经存在该商品
- 如果存在，则数量累加
- 如果不存在，则新增购物车记录
- 返回成功

注意：
如果累加后的数量超过库存，需要返回"库存不足"。

3. 修改购物车数量

PUT /api/cart/update

请求参数：

{
  "cartId": 1,
  "quantity": 2
}

逻辑：

- cartId 不存在返回错误
- quantity 必须大于 0
- 判断商品库存是否足够
- 更新购物车数量

4. 删除购物车商品

DELETE /api/cart/delete/{cartId}

逻辑：

- 根据 cartId 删除购物车记录
- 删除成功返回成功

==================================================
四、订单接口 OrderController
==================================================

1. 创建订单

POST /api/order/create

请求参数：

{
  "userId": 1,
  "receiverName": "张三",
  "receiverPhone": "13800000000",
  "receiverAddress": "广东省广州市天河区"
}

创建订单逻辑：

- 判断用户是否存在
- 校验 receiverName、receiverPhone、receiverAddress 不能为空
- 根据 userId 查询购物车
- 如果购物车为空，返回"购物车为空"
- 根据购物车中的 productId 查询商品真实价格和库存
- 判断商品是否存在
- 判断商品是否上架
- 判断库存是否足够
- 订单金额必须由后端计算
- 生成订单编号，例如使用时间戳
- 保存 orders 订单主表
- 保存 order_item 订单详情
- 扣减商品库存
- 清空当前用户购物车
- 返回订单信息

注意：
前端不能传订单金额。
后端不能相信前端价格。
订单总金额必须以后端查询到的商品价格为准。

2. 我的订单

GET /api/order/list/{userId}

逻辑：

- 根据 userId 查询订单列表
- 返回订单主信息
- 如果方便，可以一起返回订单商品明细
- 按创建时间倒序排列

OrderVO 字段建议：

- Long id
- String orderNo
- Long userId
- BigDecimal totalAmount
- String receiverName
- String receiverPhone
- String receiverAddress
- Integer status
- LocalDateTime createTime
- List<OrderItem> items

3. 模拟支付

PUT /api/order/pay/{orderId}

逻辑：

- 根据 orderId 查询订单
- 订单不存在返回错误
- 只有 status = 0 的待支付订单可以支付
- 支付成功后将 status 改为 1
- 如果订单不是待支付状态，返回错误

==================================================
五、管理员接口 AdminController
==================================================

1. 管理员登录

POST /api/admin/login

请求参数：

{
  "username": "admin",
  "password": "123456"
}

逻辑：

- 根据 username 查询管理员
- 管理员不存在返回错误
- 密码错误返回错误
- 登录成功返回：
  - id
  - username

2. 后台商品列表

GET /api/admin/product/list

逻辑：

- 返回所有商品
- 包括上架和下架

3. 新增商品

POST /api/admin/product/add

请求参数：

{
  "name": "无线蓝牙耳机",
  "category": "数码配件",
  "price": 199.00,
  "stock": 58,
  "imageUrl": "https://picsum.photos/300/300?1",
  "description": "高音质蓝牙耳机，适合日常使用",
  "status": 1
}

逻辑：

- 校验 name、price、stock 不能为空
- 保存商品
- 返回成功

4. 修改商品

PUT /api/admin/product/update

请求参数需要包含 id。

逻辑：

- 判断商品是否存在
- 更新商品信息
- 返回成功

5. 删除商品

DELETE /api/admin/product/delete/{id}

逻辑：

- 判断商品是否存在
- 存在则直接物理删除
- 返回成功

6. 后台订单列表

GET /api/admin/order/list

逻辑：

- 返回所有订单
- 包含用户 ID、订单编号、订单金额、收货人、手机号、地址、订单状态、创建时间
- 按创建时间倒序排列

7. 订单发货

PUT /api/admin/order/ship/{orderId}

逻辑：

- 根据 orderId 查询订单
- 订单不存在返回错误
- 只有 status = 1 的已支付订单可以发货
- 发货后将 status 改为 2
- 已发货订单不能重复发货
- 待支付订单不能发货

==================================================
接口开发完成后，请做以下检查：
==================================================

1. 后端能正常启动。
2. /api/product/list 可以正常返回商品列表。
3. 用户注册、登录可以正常使用。
4. 加入购物车可以正常使用。
5. 创建订单可以扣库存、清空购物车、生成订单和订单详情。
6. 模拟支付可以把订单状态从 0 改为 1。
7. 管理员发货可以把订单状态从 1 改为 2。
8. 所有接口返回格式统一。
9. 前后端字段命名保持一致，尤其是 imageUrl 和 image_url 的映射。
10. 请更新 docs/api.md，写明每个接口的路径、请求方式、参数和返回示例。
```
