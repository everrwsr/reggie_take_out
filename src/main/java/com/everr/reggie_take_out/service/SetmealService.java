package com.everr.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.everr.reggie_take_out.dto.SetmealDto;
import com.everr.reggie_take_out.entiy.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保持村套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);
    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
