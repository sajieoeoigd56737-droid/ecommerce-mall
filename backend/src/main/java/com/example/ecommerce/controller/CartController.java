package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.dto.CartDTO;
import com.example.ecommerce.dto.CartUpdateDTO;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.mapper.UserMapper;
import com.example.ecommerce.vo.CartVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartMapper cartMapper;
    private final UserMapper userMapper;
    private final ProductMapper productMapper;

    @GetMapping("/list/{userId}")
    public Result<List<CartVO>> list(@PathVariable Long userId) {
        return Result.success(cartMapper.selectCartVOList(userId));
    }

    @PostMapping("/add")
    public Result<Void> add(@RequestBody CartDTO request) {
        if (request == null || request.getUserId() == null || request.getProductId() == null) {
            return Result.error("参数不能为空");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            return Result.error("商品数量必须大于0");
        }
        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            return Result.error("用户不存在");
        }
        Product product = productMapper.selectById(request.getProductId());
        if (product == null) {
            return Result.error("商品不存在");
        }
        if (!Integer.valueOf(1).equals(product.getStatus())) {
            return Result.error("商品已下架");
        }

        Cart cart = cartMapper.selectOne(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, request.getUserId())
                .eq(Cart::getProductId, request.getProductId()));
        int quantity = request.getQuantity();
        if (cart != null) {
            quantity += cart.getQuantity();
        }
        if (quantity > product.getStock()) {
            return Result.error("库存不足");
        }

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(request.getUserId());
            cart.setProductId(request.getProductId());
            cart.setQuantity(request.getQuantity());
            cartMapper.insert(cart);
        } else {
            cart.setQuantity(quantity);
            cartMapper.updateById(cart);
        }
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody CartUpdateDTO request) {
        if (request == null || request.getCartId() == null) {
            return Result.error("购物车记录不存在");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            return Result.error("商品数量必须大于0");
        }
        Cart cart = cartMapper.selectById(request.getCartId());
        if (cart == null) {
            return Result.error("购物车记录不存在");
        }
        Product product = productMapper.selectById(cart.getProductId());
        if (product == null) {
            return Result.error("商品不存在");
        }
        if (request.getQuantity() > product.getStock()) {
            return Result.error("库存不足");
        }
        cart.setQuantity(request.getQuantity());
        cartMapper.updateById(cart);
        return Result.success();
    }

    @DeleteMapping("/delete/{cartId}")
    public Result<Void> delete(@PathVariable Long cartId) {
        cartMapper.deleteById(cartId);
        return Result.success();
    }
}
