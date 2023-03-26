package com.everr.reggie_take_out.dto;


import com.everr.reggie_take_out.entiy.Setmeal;
import com.everr.reggie_take_out.entiy.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
