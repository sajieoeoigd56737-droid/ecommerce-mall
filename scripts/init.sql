CREATE DATABASE IF NOT EXISTS `ecommerce_mall`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `ecommerce_mall`;

DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `cart`;
DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `admin`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `nickname` VARCHAR(50) DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `admin` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL,
    `password` VARCHAR(100) NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_admin_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

CREATE TABLE `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `category` VARCHAR(50) DEFAULT NULL,
    `price` DECIMAL(10, 2) NOT NULL,
    `stock` INT NOT NULL DEFAULT 0,
    `image_url` VARCHAR(255) DEFAULT NULL,
    `description` TEXT,
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1 上架，0 下架',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

CREATE TABLE `cart` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL DEFAULT 1,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

CREATE TABLE `orders` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_no` VARCHAR(50) NOT NULL,
    `user_id` BIGINT NOT NULL,
    `total_amount` DECIMAL(10, 2) NOT NULL,
    `receiver_name` VARCHAR(50) DEFAULT NULL,
    `receiver_phone` VARCHAR(20) DEFAULT NULL,
    `receiver_address` VARCHAR(255) DEFAULT NULL,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 待支付，1 已支付，2 已发货，3 已完成，4 已取消',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_orders_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `product_name` VARCHAR(100) DEFAULT NULL,
    `product_price` DECIMAL(10, 2) DEFAULT NULL,
    `quantity` INT DEFAULT NULL,
    `subtotal` DECIMAL(10, 2) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

INSERT INTO `user` (`username`, `password`, `nickname`)
VALUES ('user', '123456', '测试用户');

INSERT INTO `admin` (`username`, `password`)
VALUES ('admin', '123456');

INSERT INTO `product`
    (`name`, `category`, `price`, `stock`, `image_url`, `description`, `status`)
VALUES
    ('无线蓝牙耳机', '数码配件', 199.00, 58, '/images/products/wireless-earbuds.png', '轻便易携带的无线蓝牙耳机。', 1),
    ('不锈钢保温杯', '家居日用', 89.00, 120, '/images/products/thermal-bottle.png', '适合日常使用的不锈钢保温杯。', 1),
    ('北欧简约台灯', '家居照明', 129.00, 36, '/images/products/nordic-desk-lamp.png', '简约风格桌面照明台灯。', 1),
    ('纯棉四件套', '家居纺织', 259.00, 15, '/images/products/cotton-bedding-set.png', '柔软亲肤的纯棉床品四件套。', 1),
    ('智能手环', '智能设备', 159.00, 42, '/images/products/smart-band.png', '支持基础运动记录的智能手环。', 1);
