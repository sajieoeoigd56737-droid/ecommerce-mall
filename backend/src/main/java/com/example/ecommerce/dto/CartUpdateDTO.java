package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class CartUpdateDTO {

    private Long cartId;
    private Integer quantity;
}
