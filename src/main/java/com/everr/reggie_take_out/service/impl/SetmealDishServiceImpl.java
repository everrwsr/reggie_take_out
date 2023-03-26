package com.everr.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.everr.reggie_take_out.dto.SetmealDto;
import com.everr.reggie_take_out.entiy.SetmealDish;
import com.everr.reggie_take_out.mapper.SetmealDishMapper;
import com.everr.reggie_take_out.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {




}
