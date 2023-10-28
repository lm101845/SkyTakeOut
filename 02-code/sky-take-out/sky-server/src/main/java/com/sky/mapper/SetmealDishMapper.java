package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author liming
 * @Date 2023/10/28 22:49
 * @Description
 **/

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id获取套餐id
     * @param dishIds
     * @return
     */
    //select setmeal_id from setmeal_dish where dish_id in (1,2,3)
    List<Long> getSeatMealIdsByDishIds(List<Long> dishIds);
}
