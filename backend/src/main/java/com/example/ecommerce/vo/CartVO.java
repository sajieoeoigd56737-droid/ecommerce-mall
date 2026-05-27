package com.example.ecommerce.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartVO {

    private Long cartId;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
