package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class CartDTO {

    private Long userId;
    private Long productId;
    private Integer quantity;
}
