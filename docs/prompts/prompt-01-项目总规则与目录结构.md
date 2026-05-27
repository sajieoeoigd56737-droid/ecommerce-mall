# 第 1 部分：项目总规则与目录结构提示词

```text
你现在是一个全栈开发工程师，请帮我开发一个"电商商城系统 MVP"。

这是一个课程作业项目，不需要商业级复杂功能，只需要完成基础可运行版本。

项目要求：
1. 必须能本地运行。
2. 必须前后端分离。
3. 必须连接真实 MySQL 数据库。
4. 不要使用 mock 数据代替后端接口。
5. 不要做过度复杂架构。
6. 不要额外添加需求之外的功能。
7. 重点是完成基本电商业务闭环。

项目名称：

ecommerce-mall

项目核心流程：

用户端：
用户注册/登录
→ 浏览商品列表
→ 查看商品详情
→ 加入购物车
→ 查看购物车
→ 提交订单
→ 模拟支付
→ 查看我的订单

管理员端：
管理员登录
→ 查看商品列表
→ 新增商品
→ 修改商品
→ 删除商品
→ 查看订单列表
→ 对已支付订单进行发货

技术栈要求：

后端：
- Java
- Spring Boot
- Maven
- MyBatis-Plus
- MySQL
- Lombok

前端：
- Vue 3
- Vite
- Element Plus
- Axios
- Vue Router

不要使用：
- Redis
- RabbitMQ / Kafka
- 微服务
- Docker 强依赖
- 真实支付
- 复杂权限系统
- 复杂 JWT 权限认证
- 图片上传服务
- Tailwind CSS
- TypeScript
- Pinia

图片字段直接使用 image_url 字符串即可。

请严格按照以下目录结构创建项目：

ecommerce-mall/
├── frontend/                     # 前端 Vue 3 项目
├── backend/                      # 后端 Spring Boot 项目
├── docs/                         # 项目文档
├── scripts/                      # 数据库初始化脚本、启动脚本等
├── config/                       # 全局配置，可选
├── shared/                       # 前后端共享内容，可选
├── .env.example                  # 环境变量示例
├── README.md                     # 项目说明文档
├── docker-compose.yml            # 可选，不强制使用
├── package.json                  # 如果使用 monorepo 可以保留
└── pnpm-workspace.yaml           # 如果使用 pnpm workspace 可以保留

命名要求：

1. 项目根目录必须叫 ecommerce-mall。
2. 前端项目必须放在 frontend/。
3. 后端项目必须放在 backend/。
4. 数据库初始化文件必须放在 scripts/init.sql。
5. 文档必须放在 docs/。
6. 不要生成 mall-frontend。
7. 不要生成 mall-backend。
8. 不要生成 mall_mvp.sql。
9. 数据库名使用 ecommerce_mall。
10. 后端 Java 包名使用 com.example.ecommerce。
11. 后端启动类命名为 EcommerceMallApplication.java。

请先只完成：
1. 创建项目根目录结构。
2. 创建 frontend、backend、docs、scripts、config、shared 目录。
3. 创建根目录 README.md 初稿。
4. 创建根目录 .env.example。
5. 创建 docs 目录下的 requirements.md、api.md、database.md。
6. 创建 scripts/init.sql 空文件。

暂时不要写完整前后端业务代码，先完成项目骨架。
```
