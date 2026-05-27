# 第 3 部分：后端基础工程搭建提示词

```text
请继续完成 ecommerce-mall/backend 后端基础工程。

后端技术栈：

- Java
- Spring Boot
- Maven
- MyBatis-Plus
- MySQL
- Lombok

后端路径：

ecommerce-mall/backend

Java 包名：

com.example.ecommerce

启动类：

EcommerceMallApplication.java

后端端口：

8080

数据库：

ecommerce_mall

请创建标准 Spring Boot Maven 项目，目录结构如下：

backend/
├── src/main/java/com/example/ecommerce/
│   ├── common/
│   │   └── Result.java
│   ├── config/
│   │   └── CorsConfig.java
│   ├── controller/
│   │   ├── UserController.java
│   │   ├── ProductController.java
│   │   ├── CartController.java
│   │   ├── OrderController.java
│   │   └── AdminController.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── Admin.java
│   │   ├── Product.java
│   │   ├── Cart.java
│   │   ├── Orders.java
│   │   └── OrderItem.java
│   ├── mapper/
│   │   ├── UserMapper.java
│   │   ├── AdminMapper.java
│   │   ├── ProductMapper.java
│   │   ├── CartMapper.java
│   │   ├── OrdersMapper.java
│   │   └── OrderItemMapper.java
│   ├── dto/
│   │   ├── LoginDTO.java
│   │   ├── RegisterDTO.java
│   │   ├── CartDTO.java
│   │   ├── CartUpdateDTO.java
│   │   ├── OrderCreateDTO.java
│   │   └── ProductDTO.java
│   ├── vo/
│   │   ├── CartVO.java
│   │   └── OrderVO.java
│   └── EcommerceMallApplication.java
├── src/main/resources/
│   └── application.yml
└── pom.xml

如果需要 service 层，可以增加：

service/
service/impl/

但不要过度封装，保持 MVP 项目简单清晰。

请完成以下内容：

1. pom.xml
   - 引入 Spring Web
   - 引入 MySQL Driver
   - 引入 MyBatis-Plus
   - 引入 Lombok

2. application.yml
   - 配置端口 8080
   - 配置 MySQL 连接
   - 数据库名称 ecommerce_mall
   - 开启 MyBatis-Plus 下划线转驼峰

3. Result.java
   - 统一返回格式
   - 成功返回 code=200
   - 失败返回 code=500

返回格式：

成功：
{
  "code": 200,
  "message": "成功",
  "data": {}
}

失败：
{
  "code": 500,
  "message": "错误原因",
  "data": null
}

4. CorsConfig.java
   - 配置跨域
   - 允许前端 http://localhost:5173 访问
   - 允许 GET、POST、PUT、DELETE

5. entity 实体类
   - 对应 user、admin、product、cart、orders、order_item 表
   - Java 使用驼峰命名
   - 数据库字段使用下划线命名
   - 使用 Lombok @Data
   - 必要时使用 @TableName

6. mapper
   - 每个实体类对应一个 Mapper
   - 继承 MyBatis-Plus 的 BaseMapper

7. dto
   - LoginDTO
   - RegisterDTO
   - CartDTO
   - CartUpdateDTO
   - OrderCreateDTO
   - ProductDTO

8. vo
   - CartVO：购物车返回展示对象
   - OrderVO：订单返回展示对象

9. 创建空 Controller 文件
   - UserController
   - ProductController
   - CartController
   - OrderController
   - AdminController

要求：

1. 后端项目可以正常启动。
2. 不要先写复杂业务，先保证基础工程能跑。
3. 启动后不报错。
4. 请在 README.md 中补充后端启动方式：
   cd backend
   mvn spring-boot:run
```
