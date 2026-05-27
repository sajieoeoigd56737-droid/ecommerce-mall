package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.dto.LoginDTO;
import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Admin;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.mapper.AdminMapper;
import com.example.ecommerce.mapper.OrdersMapper;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.vo.AdminInfoVO;
import com.example.ecommerce.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminMapper adminMapper;
    private final ProductMapper productMapper;
    private final OrdersMapper ordersMapper;
    private final OrderService orderService;

    @PostMapping("/login")
    public Result<AdminInfoVO> login(@RequestBody LoginDTO request) {
        if (request == null || !StringUtils.hasText(request.getUsername())) {
            return Result.error("管理员账号不能为空");
        }
        if (!StringUtils.hasText(request.getPassword())) {
            return Result.error("密码不能为空");
        }
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>()
                .eq(Admin::getUsername, request.getUsername().trim()));
        if (admin == null) {
            return Result.error("管理员不存在");
        }
        if (!admin.getPassword().equals(request.getPassword())) {
            return Result.error("密码错误");
        }

        AdminInfoVO adminInfo = new AdminInfoVO();
        adminInfo.setId(admin.getId());
        adminInfo.setUsername(admin.getUsername());
        return Result.success(adminInfo);
    }

    @GetMapping("/product/list")
    public Result<List<Product>> productList() {
        return Result.success(productMapper.selectList(new LambdaQueryWrapper<Product>()
                .orderByDesc(Product::getCreateTime)
                .orderByDesc(Product::getId)));
    }

    @PostMapping("/product/add")
    public Result<Void> addProduct(@RequestBody ProductDTO request) {
        Result<Void> validation = validateProduct(request, false);
        if (validation != null) {
            return validation;
        }
        Product product = toProduct(request);
        if (product.getStatus() == null) {
            product.setStatus(1);
        }
        productMapper.insert(product);
        return Result.success();
    }

    @PutMapping("/product/update")
    public Result<Void> updateProduct(@RequestBody ProductDTO request) {
        Result<Void> validation = validateProduct(request, true);
        if (validation != null) {
            return validation;
        }
        if (productMapper.selectById(request.getId()) == null) {
            return Result.error("商品不存在");
        }
        productMapper.updateById(toProduct(request));
        return Result.success();
    }

    @DeleteMapping("/product/delete/{id}")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        if (productMapper.selectById(id) == null) {
            return Result.error("商品不存在");
        }
        productMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/order/list")
    public Result<List<OrderVO>> orderList() {
        return Result.success(orderService.listAllOrders());
    }

    @PutMapping("/order/ship/{orderId}")
    public Result<Void> ship(@PathVariable Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            return Result.error("订单不存在");
        }
        if (!Integer.valueOf(1).equals(order.getStatus())) {
            return Result.error("只有已支付订单可以发货");
        }
        int changed = ordersMapper.update(null, new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getId, orderId)
                .eq(Orders::getStatus, 1)
                .set(Orders::getStatus, 2));
        if (changed != 1) {
            return Result.error("订单状态已变更，请刷新后重试");
        }
        return Result.success();
    }

    private Result<Void> validateProduct(ProductDTO request, boolean requireId) {
        if (request == null) {
            return Result.error("商品信息不能为空");
        }
        if (requireId && request.getId() == null) {
            return Result.error("商品ID不能为空");
        }
        if (!StringUtils.hasText(request.getName())) {
            return Result.error("商品名称不能为空");
        }
        if (request.getPrice() == null) {
            return Result.error("商品价格不能为空");
        }
        if (request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            return Result.error("商品价格不能小于0");
        }
        if (request.getStock() == null) {
            return Result.error("商品库存不能为空");
        }
        if (request.getStock() < 0) {
            return Result.error("商品库存不能小于0");
        }
        return null;
    }

    private Product toProduct(ProductDTO request) {
        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName().trim());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setDescription(request.getDescription());
        product.setStatus(request.getStatus());
        return product;
    }
}
