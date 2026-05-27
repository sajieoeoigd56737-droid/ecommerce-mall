package com.example.ecommerce.dto;

import lombok.Data;

@Data
public class OrderCreateDTO {

    private Long userId;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
}
