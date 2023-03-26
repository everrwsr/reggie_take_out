package com.everr.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.everr.reggie_take_out.common.CustomException;
import com.everr.reggie_take_out.dto.SetmealDto;
import com.everr.reggie_take_out.entiy.Setmeal;
import com.everr.reggie_take_out.entiy.SetmealDish;
import com.everr.reggie_take_out.mapper.SetmealDishMapper;
import com.everr.reggie_take_out.mapper.SetmealMapper;
import com.everr.reggie_take_out.service.SetmealDishService;
import com.everr.reggie_take_out.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 1.先保存基本信息到setmeal表。
        this.save(setmealDto);
        // 2.保存套餐对应的菜品信息到setmeal_dish表。
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        // Field 'setmeal_id' doesn't have a default value] with root cause
        // 所以需要处理setmeal_id字段。
        setmealDishes.stream()
                .map(
                        (item) -> {
                            item.setSetmealId(setmealDto.getId());
                            return item;
                        })
                .collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);


        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除");
        }


        // 如果可以删除出，先删除套餐表的数据

        this.removeByIds(ids);

        // 删除关系表的数据


        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper);


    }


}
