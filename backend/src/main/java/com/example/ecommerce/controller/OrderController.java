package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.dto.OrderCreateDTO;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Result<OrderVO> create(@RequestBody OrderCreateDTO request) {
        return Result.success(orderService.createOrder(request));
    }

    @GetMapping("/list/{userId}")
    public Result<List<OrderVO>> list(@PathVariable Long userId) {
        return Result.success(orderService.listUserOrders(userId));
    }

    @PutMapping("/pay/{orderId}")
    public Result<Void> pay(@PathVariable Long orderId) {
        orderService.pay(orderId);
        return Result.success();
    }
}
