package com.everr.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.everr.reggie_take_out.common.BaseContext;
import com.everr.reggie_take_out.common.R;
import com.everr.reggie_take_out.entiy.ShoppingCart;
import com.everr.reggie_take_out.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {

        log.info("购物车数据为：{}", shoppingCart);

        // 设置用户id,指定当前是哪个用户的购物车数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询当前菜品或者套餐是否在购物车中

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId, dishId);

        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());


        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null) {
            Integer number = cartServiceOne.getNumber();

            cartServiceOne.setNumber(number + 1);


            shoppingCartService.updateById(cartServiceOne);

        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;

        }


        //如果已经存在，那么在原来数量加1

        // 如不存在，添加到购物车，默认数量是1


        return R.success(cartServiceOne);

    }

    /**
     * 查看购物车
     *
     * @return
     */

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        log.info("查看购物车");


        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);


        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean() {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");

    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(dishId != null){
            // 此时为dish 需判断数量， 为1 直接删除
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
            lambdaQueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
            ShoppingCart one = shoppingCartService.getOne(lambdaQueryWrapper);
            Integer number = one.getNumber();
            number -= 1;
            one.setNumber(number);
            shoppingCartService.updateById(one);
            if(number <= 0){
                shoppingCartService.remove(lambdaQueryWrapper);

            }
            return R.success(one);

        }else{
            return null;
        }
    }
}
