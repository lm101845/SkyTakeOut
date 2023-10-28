package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @Author liming
 * @Date 2023/10/28 15:26
 * @Description
 **/

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 新增菜品和对应的口味数据
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //1.向菜品表插入1条数据
        dishMapper.insert(dish);

        //获取insert语句执行后的主键值
        Long dishId = dish.getId();
        log.info("新增菜品后，菜品的主键值为：{}",dishId);
        //2.向口味表插入N条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
            //向口味表插入N条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //开始分页
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        //vo是进行页面展示，dto是前后端数据交互的，pojo是对应数据库表字段
        //vo一般是后端给前端用的，dto一般是前端给后端用的，就这么记
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //我觉得老师的开发思路很好 先用注释捋清楚 然后再开发
        //后面会加redis，以后菜品是直接查redis缓存了，不会给数据库造成压力
        //这个应该是前端如果停售，删除的方块就是灰的，点不了
        //判断当前菜品是否能够被删除——是否存在起售中的菜品??
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                //当前菜品处于起售中中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //判断当前菜品是否能够被删除——是否被套餐关联了
        List<Long> setmealIds = setmealDishMapper.getSeatMealIdsByDishIds(ids);
        if(setmealIds != null && setmealIds.size() > 0){
            //当前菜品被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品表中的菜品数据
        //未优化前：逐条删除菜品表中的菜品数据
        //for (Long id : ids) {
        //    dishMapper.deleteById(id);
        //    //删除菜品关联的口味数据——不管你有没有，我都尝试删除，就不去查了
        //    dishFlavorMapper.deleteByDishId(id);
        //}

        //这是告诉大家调优是根据你的开发过程来的，不是一开始就调优的
        //代码优化：批量删除菜品表中的菜品数据
        //sql:delete from dish where id in (?,?,?)
        dishMapper.deleteByIds(ids);

        //删除菜品关联的口味数据
        //sql:delete from dish_flavor where dish_id in (?,?,?)
        dishFlavorMapper.deleteByDishIds(ids);
    }
}
