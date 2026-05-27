# 第 5 部分：前端基础工程搭建提示词

```text
请继续完成 ecommerce-mall/frontend 前端基础工程。

前端技术栈：

- Vue 3
- Vite
- Element Plus
- Axios
- Vue Router

不要使用：

- TypeScript
- Pinia
- Tailwind CSS

前端路径：

ecommerce-mall/frontend

前端端口：

5173

前端接口地址：

http://localhost:8080/api

请创建 Vue 3 + Vite 项目，并完成以下目录结构：

frontend/
├── src/
│   ├── api/
│   │   ├── user.js
│   │   ├── product.js
│   │   ├── cart.js
│   │   ├── order.js
│   │   └── admin.js
│   ├── router/
│   │   └── index.js
│   ├── utils/
│   │   └── request.js
│   ├── views/
│   │   ├── Login.vue
│   │   ├── Register.vue
│   │   ├── Home.vue
│   │   ├── ProductDetail.vue
│   │   ├── Cart.vue
│   │   ├── Orders.vue
│   │   ├── AdminLogin.vue
│   │   ├── AdminProduct.vue
│   │   └── AdminOrder.vue
│   ├── App.vue
│   └── main.js
├── package.json
├── vite.config.js
├── .env.example
└── index.html

请完成以下内容：

一、安装依赖

需要安装：

- element-plus
- axios
- vue-router

二、main.js

要求：

- 创建 Vue 应用
- 引入 Element Plus
- 引入 Element Plus 样式
- 引入 router
- 挂载 App

三、router/index.js

配置路由：

/                      重定向到 /home
/login                 用户登录
/register              用户注册
/home                  商城首页
/product/:id           商品详情
/cart                  购物车
/orders                我的订单

/admin/login           管理员登录
/admin/products        商品管理
/admin/orders          订单管理

四、utils/request.js

要求：

- 使用 axios.create
- baseURL 从 import.meta.env.VITE_API_BASE_URL 读取
- timeout 设置为 10000
- 响应拦截器直接返回 response.data
- 如果 response.data.code !== 200，使用 Element Plus 的 ElMessage 显示错误
- 请求失败时显示"请求失败，请检查网络或后端服务"

五、frontend/.env.example

内容：

VITE_API_BASE_URL=http://localhost:8080/api

六、api 目录

请封装以下 API 文件：

1. user.js

- register(data)
- login(data)
- getUserInfo(id)

2. product.js

- getProductList()
- getProductDetail(id)

3. cart.js

- getCartList(userId)
- addCart(data)
- updateCart(data)
- deleteCart(cartId)

4. order.js

- createOrder(data)
- getOrderList(userId)
- payOrder(orderId)

5. admin.js

- adminLogin(data)
- getAdminProductList()
- addProduct(data)
- updateProduct(data)
- deleteProduct(id)
- getAdminOrderList()
- shipOrder(orderId)

七、App.vue

要求：

- 只保留 router-view
- 不要写复杂布局

八、页面占位

请先创建以下空页面或简单占位页面，保证路由不报错：

- Login.vue
- Register.vue
- Home.vue
- ProductDetail.vue
- Cart.vue
- Orders.vue
- AdminLogin.vue
- AdminProduct.vue
- AdminOrder.vue

每个页面可以先显示页面标题。

九、UI 总体风格变量

可以在 App.vue 或每个页面样式中使用统一风格：

- 背景：#f8f1e8、#fffaf3
- 主色：#e95b1c 或 #e66a2c
- 标题：#2f2118
- 正文：#6f6258
- 边框：#eadfd2
- 卡片背景：#ffffff
- 圆角：16px
- 阴影：0 10px 30px rgba(80, 50, 20, 0.08)

要求：

1. 前端项目可以 npm install。
2. 前端项目可以 npm run dev。
3. 访问 /home 不报错。
4. 路由跳转正常。
5. API 封装路径和后端接口保持一致。
6. 请更新 README.md 中的前端启动方式：
   cd frontend
   npm install
   npm run dev
```
