# ecommerce-mall

电商商城系统 MVP，采用前后端分离结构，后端使用 Spring Boot 与 MyBatis-Plus 连接 MySQL 数据库。

## 后端启动

### 环境要求

- Java 17 或更高版本
- Maven 3.6.3 或更高版本
- MySQL 8.0 或更高版本

### 初始化数据库

在 MySQL 中执行初始化脚本：

```bash
mysql -u root -p < scripts/init.sql
```

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

服务启动后监听 `http://localhost:8080`，并允许 `http://localhost:5173` 发起跨域 API 请求。

## 前端启动

### 环境要求

- Node.js 20.19 或更高版本
- npm 10 或更高版本

### 配置接口地址

前端默认使用 `.env.example` 中的后端接口地址：

```bash
VITE_API_BASE_URL=http://localhost:8080/api
```

### 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端开发服务监听 `http://localhost:5173`。
