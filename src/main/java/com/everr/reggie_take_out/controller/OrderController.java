package com.everr.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.everr.reggie_take_out.common.R;
import com.everr.reggie_take_out.entiy.Orders;
import com.everr.reggie_take_out.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    /**
     * 后台管理订单信息分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime, String endTime){
        log.info("page = {},pageSize = {}",page,pageSize);

        //构造分页构造器
        Page<Orders> pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(number != null,Orders::getId, number);
        queryWrapper.ge(beginTime != null,Orders::getOrderTime,beginTime);
        queryWrapper.le(endTime != null,Orders::getOrderTime,endTime);
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getCheckoutTime);

        //执行查询
        orderService.page(pageInfo,queryWrapper);

        List<Orders> records = pageInfo.getRecords();
        records = records.stream().map((item) -> {

            item.setUserName("用户" + item.getUserId());

            return item;
        }).collect(Collectors.toList());

        return R.success(pageInfo);
    }

    /**
     * 派送订单
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> updateOrder(@RequestBody Orders orders){
        //构造条件构造器
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        //添加过滤条件
        updateWrapper.eq(Orders::getId, orders.getId());
        updateWrapper.set(Orders::getStatus,orders.getStatus());
        orderService.update(updateWrapper);

        return R.success("订单派送成功");
    }


}
