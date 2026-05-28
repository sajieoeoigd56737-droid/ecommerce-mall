# ecommerce-mall

电商商城系统 MVP，采用前后端分离结构，后端使用 Spring Boot 与 MyBatis-Plus 连接 MySQL 数据库。

## 功能概览

- 用户端：首页商品列表、商品详情、用户注册登录、购物车、提交订单、我的订单、模拟支付。
- 管理端：管理员登录、商品管理、订单管理、订单发货。
- 开发联调：前端通过 Vite `/api` 代理访问后端，支持同一局域网设备访问前端页面并正常调用接口。

## 项目结构

```text
backend/        Spring Boot 后端服务
frontend/       Vue 3 + Vite 前端项目
scripts/        数据库初始化脚本
docs/           接口文档与开发提示词
```

## 后端启动

### 环境要求

- Java 17
- Maven 3.6.3 或更高版本
- MySQL 8.0 或更高版本

### 初始化数据库

在 MySQL 中执行初始化脚本：

```bash
mysql -u root -p < scripts/init.sql
```

脚本会创建 `ecommerce_mall` 数据库、初始化基础表，并写入测试账号和示例商品。

默认账号：

| 角色 | 用户名 | 密码 | 入口 |
| --- | --- | --- | --- |
| 普通用户 | `user` | `123456` | `/login` |
| 管理员 | `admin` | `123456` | `/admin/login` |

### 数据库配置

后端默认连接本机 `3306` 端口上的 `ecommerce_mall` 数据库，并使用 `root` 账号。可以通过以下环境变量覆盖连接配置：

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_USERNAME=root
export DB_PASSWORD=your_password
```

### 启动服务

```bash
cd backend
mvn spring-boot:run
```

服务启动后监听 `http://localhost:8080`。前端开发服务会在同源 `/api` 路径下代理请求到该服务，后端也允许通过开发端口 `5173` 打开的局域网页面发起 API 请求。

后端验证：

```bash
cd backend
mvn test
```

## 前端启动

### 环境要求

- Node.js 20.19 或更高版本
- npm 10 或更高版本

### 配置接口地址

本地开发默认无需配置接口地址。浏览器向前端开发服务的 `/api` 发起请求，由 Vite 转发到本机后端，因此通过局域网打开前端页面时也能正常加载数据。

```bash
# 仅在部署前端并需要连接指定后端时配置
VITE_API_BASE_URL=https://api.example.com/api
```

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端开发服务监听所有网卡上的 `5173` 端口。本机可访问 `http://localhost:5173`，同一局域网设备可访问终端输出的 `Network` 地址，例如：

```text
Local:   http://localhost:5173/
Network: http://你的局域网 IP:5173/
```

如果终端输出多个 `Network` 地址，优先使用和同学处于同一 Wi-Fi/局域网网段的地址；虚拟网卡地址可能无法从其他设备访问。

前端构建验证：

```bash
cd frontend
npm run build
```

## 联调说明

- 前端请求默认使用 `/api`，由 Vite 代理到 `http://127.0.0.1:8080`。
- 本地开发一般不要配置 `VITE_API_BASE_URL`，否则可能绕过 Vite 代理，导致局域网设备访问失败。
- 只有部署到独立前端域名时，才需要在 `.env` 中设置 `VITE_API_BASE_URL=https://api.example.com/api`。
