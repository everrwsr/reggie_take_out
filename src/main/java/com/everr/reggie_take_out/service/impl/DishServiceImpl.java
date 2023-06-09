package com.everr.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.everr.reggie_take_out.dto.DishDto;
import com.everr.reggie_take_out.entiy.Dish;
import com.everr.reggie_take_out.entiy.DishFlavor;
import com.everr.reggie_take_out.mapper.DishMapper;
import com.everr.reggie_take_out.service.DishFlavorService;
import com.everr.reggie_take_out.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * @param dishDto
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {


        //保存菜品基本信息到dish
        this.save(dishDto);

        Long dishId = dishDto.getId();
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
                    item.setDishId(dishId);
                    return item;
                }
        ).collect(Collectors.toList());


        //保存菜品口味数据到菜品口味表

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id 查询菜品信息和对应口味信息
     *
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id) {

        // 查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);


        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        // 查询当前菜品对应的口味信息，从dish_flavor 表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);
        return dishDto;
    }

    /**
     * 更新菜品信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新基本信息dish
        this.updateById(dishDto);


        // 清理当前次啊品对应口味信息数据

        LambdaQueryWrapper<DishFlavor>  queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());


        dishFlavorService.remove(queryWrapper);


        // 添加当前提交的信息
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
                    item.setDishId(dishDto.getId());
                    return item;
                }
        ).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);




    }
}
