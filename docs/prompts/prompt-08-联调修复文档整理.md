# 第 8 部分：联调、修复、文档整理提示词

```text
请对整个 ecommerce-mall 项目进行联调、检查、修复和文档整理。

项目结构：

ecommerce-mall/
├── frontend/
├── backend/
├── docs/
├── scripts/
├── config/
├── shared/
├── .env.example
├── README.md
├── docker-compose.yml
├── package.json
└── pnpm-workspace.yaml

请重点检查以下内容。

==================================================
一、项目结构检查
==================================================

确认项目目录符合要求：

1. 前端在 frontend/。
2. 后端在 backend/。
3. 数据库脚本在 scripts/init.sql。
4. 文档在 docs/。
5. 根目录有 README.md。
6. 根目录有 .env.example。
7. 不存在 mall-frontend。
8. 不存在 mall-backend。
9. 不存在 mall_mvp.sql。
10. 数据库名称统一为 ecommerce_mall。

==================================================
二、后端检查
==================================================

检查 backend 是否满足：

1. Spring Boot 可以正常启动。
2. 端口是 8080。
3. application.yml 数据库配置正确。
4. 包名是 com.example.ecommerce。
5. 启动类是 EcommerceMallApplication.java。
6. MyBatis-Plus Mapper 可以正常扫描。
7. 跨域配置生效。
8. 所有接口都以 /api 开头。
9. 所有接口返回 Result 格式。
10. 业务错误不会直接抛异常堆栈。
11. 下划线字段和驼峰实体映射正常。

需要重点测试接口：

用户：
- POST /api/user/register
- POST /api/user/login
- GET /api/user/info/{id}

商品：
- GET /api/product/list
- GET /api/product/{id}

购物车：
- GET /api/cart/list/{userId}
- POST /api/cart/add
- PUT /api/cart/update
- DELETE /api/cart/delete/{cartId}

订单：
- POST /api/order/create
- GET /api/order/list/{userId}
- PUT /api/order/pay/{orderId}

管理员：
- POST /api/admin/login
- GET /api/admin/product/list
- POST /api/admin/product/add
- PUT /api/admin/product/update
- DELETE /api/admin/product/delete/{id}
- GET /api/admin/order/list
- PUT /api/admin/order/ship/{orderId}

==================================================
三、数据库检查
==================================================

检查 scripts/init.sql：

1. 可以创建 ecommerce_mall 数据库。
2. 可以创建 6 张表。
3. 可以重复执行。
4. user 表有默认用户 user / 123456。
5. admin 表有默认管理员 admin / 123456。
6. product 表有至少 5 个商品。
7. orders 表名称正确，不要使用 order。
8. 字段名和后端实体映射一致。

==================================================
四、前端检查
==================================================

检查 frontend 是否满足：

1. npm install 可以成功。
2. npm run dev 可以启动。
3. 默认端口是 5173。
4. VITE_API_BASE_URL 配置正确。
5. request.js 正确读取 import.meta.env.VITE_API_BASE_URL。
6. API 文件路径和后端接口一致。
7. 路由配置正确。
8. 页面跳转正确。
9. localStorage 保存 userInfo 和 adminInfo。
10. 未登录访问购物车、订单时有提示。
11. 管理员未登录访问后台时有提示。

需要检查页面：

用户端：
- /login
- /register
- /home
- /product/:id
- /cart
- /orders

管理端：
- /admin/login
- /admin/products
- /admin/orders

==================================================
五、字段一致性检查
==================================================

重点检查前后端字段是否一致：

商品字段：
- id
- name
- category
- price
- stock
- imageUrl
- description
- status
- createTime

数据库中是 image_url，Java 实体类是 imageUrl，前端也应使用 imageUrl。

购物车返回字段：
- cartId
- productId
- productName
- productImage
- price
- quantity
- subtotal

订单字段：
- id
- orderNo
- userId
- totalAmount
- receiverName
- receiverPhone
- receiverAddress
- status
- createTime
- items

如果发现字段不一致，请统一修复。

==================================================
六、完整业务流程测试
==================================================

请测试以下用户端流程：

1. 打开 /home。
2. 查看商品列表。
3. 注册用户。
4. 登录用户。
5. 进入商品详情。
6. 加入购物车。
7. 打开购物车。
8. 修改商品数量。
9. 删除购物车商品。
10. 填写收货信息。
11. 提交订单。
12. 跳转我的订单。
13. 点击模拟支付。
14. 订单状态从待支付变成已支付。

请测试以下管理员端流程：

1. 打开 /admin/login。
2. 使用 admin / 123456 登录。
3. 进入商品管理。
4. 新增商品。
5. 编辑商品。
6. 删除商品。
7. 进入订单管理。
8. 查看订单列表。
9. 对已支付订单点击发货。
10. 订单状态从已支付变成已发货。

==================================================
七、文档整理
==================================================

请完善 docs/ 目录。

docs/requirements.md 包含：

1. 项目背景
2. 项目目标
3. 用户端功能
4. 管理员端功能
5. 不包含功能
6. 技术栈说明

docs/api.md 包含：

1. 用户接口
2. 商品接口
3. 购物车接口
4. 订单接口
5. 管理员接口
6. 每个接口包含：
   - 请求方式
   - 请求路径
   - 请求参数
   - 返回示例

docs/database.md 包含：

1. 数据库名称 ecommerce_mall
2. 表结构说明
3. 字段说明
4. 初始化账号
5. 初始化商品

docs/screenshots/：

- 创建 README.md 占位，说明这里用于存放项目截图。

==================================================
八、根目录 README.md 整理
==================================================

请完善根目录 README.md，内容必须包含：

1. 项目名称
2. 项目介绍
3. 技术栈
4. 功能模块
5. 项目目录结构
6. 数据库初始化方式
7. 后端启动方式
8. 前端启动方式
9. 默认测试账号
10. 常见问题
11. 演示流程

默认测试账号：

普通用户：
账号：user
密码：123456

管理员：
账号：admin
密码：123456

运行方式：

数据库：

先创建数据库并执行：

scripts/init.sql

后端：

cd backend
mvn spring-boot:run

前端：

cd frontend
npm install
npm run dev

==================================================
九、最终验收标准
==================================================

用户端必须满足：

1. 用户可以注册。
2. 用户可以登录。
3. 首页可以展示商品列表。
4. 可以查看商品详情。
5. 可以加入购物车。
6. 可以查看购物车。
7. 可以修改购物车数量。
8. 可以删除购物车商品。
9. 可以填写收货信息并提交订单。
10. 可以查看我的订单。
11. 可以模拟支付订单。

管理员端必须满足：

1. 管理员可以登录。
2. 管理员可以查看商品列表。
3. 管理员可以新增商品。
4. 管理员可以修改商品。
5. 管理员可以删除商品。
6. 管理员可以查看订单列表。
7. 管理员可以对已支付订单发货。

数据库必须满足：

1. user 表有用户数据。
2. admin 表有管理员数据。
3. product 表有商品数据。
4. cart 表可以保存购物车数据。
5. orders 表可以保存订单主信息。
6. order_item 表可以保存订单商品明细。

请修复所有影响运行和演示的问题，最终保证项目可以本地完整运行。
```
