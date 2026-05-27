package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.ecommerce.common.BusinessException;
import com.example.ecommerce.dto.OrderCreateDTO;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.entity.Orders;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.mapper.CartMapper;
import com.example.ecommerce.mapper.OrderItemMapper;
import com.example.ecommerce.mapper.OrdersMapper;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.mapper.UserMapper;
import com.example.ecommerce.vo.OrderVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final DateTimeFormatter ORDER_NO_TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private final UserMapper userMapper;
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    private final OrdersMapper ordersMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional(rollbackFor = Exception.class)
    public OrderVO createOrder(OrderCreateDTO request) {
        validateOrderRequest(request);
        if (userMapper.selectById(request.getUserId()) == null) {
            throw new BusinessException("用户不存在");
        }

        List<Cart> carts = cartMapper.selectList(new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, request.getUserId()));
        if (carts.isEmpty()) {
            throw new BusinessException("购物车为空");
        }

        List<Product> products = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cart : carts) {
            Product product = productMapper.selectById(cart.getProductId());
            validateCartProduct(product, cart.getQuantity());
            products.add(product);
            totalAmount = totalAmount.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(cart.getQuantity())));
        }

        Orders order = new Orders();
        order.setOrderNo(generateOrderNo());
        order.setUserId(request.getUserId());
        order.setTotalAmount(totalAmount);
        order.setReceiverName(request.getReceiverName().trim());
        order.setReceiverPhone(request.getReceiverPhone().trim());
        order.setReceiverAddress(request.getReceiverAddress().trim());
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        ordersMapper.insert(order);

        List<OrderItem> items = new ArrayList<>();
        for (int index = 0; index < carts.size(); index++) {
            Cart cart = carts.get(index);
            Product product = products.get(index);
            int changed = productMapper.update(null, new LambdaUpdateWrapper<Product>()
                    .eq(Product::getId, product.getId())
                    .eq(Product::getStatus, 1)
                    .ge(Product::getStock, cart.getQuantity())
                    .setSql("stock = stock - " + cart.getQuantity()));
            if (changed != 1) {
                throw new BusinessException("库存不足");
            }

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setProductPrice(product.getPrice());
            item.setQuantity(cart.getQuantity());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
            orderItemMapper.insert(item);
            items.add(item);
        }

        cartMapper.delete(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, request.getUserId()));
        OrderVO result = toOrderVO(order);
        result.setItems(items);
        return result;
    }

    public List<OrderVO> listUserOrders(Long userId) {
        List<Orders> orders = ordersMapper.selectList(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getUserId, userId)
                .orderByDesc(Orders::getCreateTime)
                .orderByDesc(Orders::getId));
        return toOrderVOList(orders);
    }

    public List<OrderVO> listAllOrders() {
        List<Orders> orders = ordersMapper.selectList(new LambdaQueryWrapper<Orders>()
                .orderByDesc(Orders::getCreateTime)
                .orderByDesc(Orders::getId));
        return toOrderVOList(orders);
    }

    @Transactional(rollbackFor = Exception.class)
    public void pay(Long orderId) {
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!Integer.valueOf(0).equals(order.getStatus())) {
            throw new BusinessException("只有待支付订单可以支付");
        }
        int changed = ordersMapper.update(null, new LambdaUpdateWrapper<Orders>()
                .eq(Orders::getId, orderId)
                .eq(Orders::getStatus, 0)
                .set(Orders::getStatus, 1));
        if (changed != 1) {
            throw new BusinessException("订单状态已变更，请刷新后重试");
        }
    }

    private void validateOrderRequest(OrderCreateDTO request) {
        if (request == null || request.getUserId() == null) {
            throw new BusinessException("用户不能为空");
        }
        if (!StringUtils.hasText(request.getReceiverName())) {
            throw new BusinessException("收货人姓名不能为空");
        }
        if (!StringUtils.hasText(request.getReceiverPhone())) {
            throw new BusinessException("收货人手机号不能为空");
        }
        if (!StringUtils.hasText(request.getReceiverAddress())) {
            throw new BusinessException("收货地址不能为空");
        }
    }

    private void validateCartProduct(Product product, Integer quantity) {
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        if (!Integer.valueOf(1).equals(product.getStatus())) {
            throw new BusinessException("商品已下架");
        }
        if (quantity == null || quantity <= 0) {
            throw new BusinessException("商品数量必须大于0");
        }
        if (product.getStock() < quantity) {
            throw new BusinessException("库存不足");
        }
    }

    private String generateOrderNo() {
        return "ORD" + LocalDateTime.now().format(ORDER_NO_TIME_FORMAT)
                + ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    private List<OrderVO> toOrderVOList(List<Orders> orders) {
        List<OrderVO> result = new ArrayList<>();
        for (Orders order : orders) {
            OrderVO orderVO = toOrderVO(order);
            orderVO.setItems(orderItemMapper.selectList(new LambdaQueryWrapper<OrderItem>()
                    .eq(OrderItem::getOrderId, order.getId())));
            result.add(orderVO);
        }
        return result;
    }

    private OrderVO toOrderVO(Orders order) {
        OrderVO orderVO = new OrderVO();
        orderVO.setId(order.getId());
        orderVO.setOrderNo(order.getOrderNo());
        orderVO.setUserId(order.getUserId());
        orderVO.setTotalAmount(order.getTotalAmount());
        orderVO.setReceiverName(order.getReceiverName());
        orderVO.setReceiverPhone(order.getReceiverPhone());
        orderVO.setReceiverAddress(order.getReceiverAddress());
        orderVO.setStatus(order.getStatus());
        orderVO.setCreateTime(order.getCreateTime());
        return orderVO;
    }
}
