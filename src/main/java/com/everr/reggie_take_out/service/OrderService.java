package com.everr.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.everr.reggie_take_out.entiy.Orders;
import com.everr.reggie_take_out.mapper.OrderMapper;
import org.springframework.core.annotation.Order;

public interface OrderService extends IService<Orders>  {
    public void submit(Orders orders);

}
