package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

/**
 * @Author liming
 * @Date 2023/11/10 22:24
 * @Description
 **/
public interface ShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
