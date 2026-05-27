package com.example.ecommerce.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.vo.CartVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CartMapper extends BaseMapper<Cart> {

    @Select("""
            SELECT c.id AS cart_id,
                   c.product_id AS product_id,
                   p.name AS product_name,
                   p.image_url AS product_image,
                   p.price AS price,
                   c.quantity AS quantity,
                   p.price * c.quantity AS subtotal
            FROM cart c
            INNER JOIN product p ON p.id = c.product_id
            WHERE c.user_id = #{userId}
            ORDER BY c.create_time DESC, c.id DESC
            """)
    List<CartVO> selectCartVOList(@Param("userId") Long userId);
}
