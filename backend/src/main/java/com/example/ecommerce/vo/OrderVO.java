package com.example.ecommerce.vo;

import com.example.ecommerce.entity.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {

    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private Integer status;
    private LocalDateTime createTime;
    private List<OrderItem> items;
}
