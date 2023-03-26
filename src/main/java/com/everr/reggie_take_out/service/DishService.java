package com.everr.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.everr.reggie_take_out.dto.DishDto;
import com.everr.reggie_take_out.entiy.Dish;

public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品对应口味数据，需要操作dish,dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
