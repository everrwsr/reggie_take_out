package com.everr.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.everr.reggie_take_out.common.CustomException;
import com.everr.reggie_take_out.entiy.Category;
import com.everr.reggie_take_out.entiy.Dish;
import com.everr.reggie_take_out.entiy.Setmeal;
import com.everr.reggie_take_out.mapper.CategoryMapper;
import com.everr.reggie_take_out.service.CategoryService;
import com.everr.reggie_take_out.service.DishService;
import com.everr.reggie_take_out.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id进行删除，删除之前需要判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类关联了菜品，不能删除");
        }
        //已经关联一个菜品，抛出业务异常


        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<Setmeal>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //已经关联一个套餐，抛出业务异常
            throw new CustomException("当前分类关联了套餐，不能删除");
        }
        // 正常删除分类
        super.removeById(id);
    }
}
