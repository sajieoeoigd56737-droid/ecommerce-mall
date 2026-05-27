package com.example.ecommerce.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String category;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private String description;
    private Integer status;
}
