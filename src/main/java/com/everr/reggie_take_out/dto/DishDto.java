package com.everr.reggie_take_out.dto;


import com.everr.reggie_take_out.entiy.Dish;
import com.everr.reggie_take_out.entiy.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
