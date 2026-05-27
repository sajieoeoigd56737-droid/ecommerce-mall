package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductMapper productMapper;

    @GetMapping("/list")
    public Result<List<Product>> list() {
        List<Product> products = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .eq(Product::getStatus, 1)
                .orderByDesc(Product::getCreateTime)
                .orderByDesc(Product::getId));
        return Result.success(products);
    }

    @GetMapping("/{id}")
    public Result<Product> detail(@PathVariable Long id) {
        Product product = productMapper.selectById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        return Result.success(product);
    }
}
