package com.everr.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.everr.reggie_take_out.entiy.User;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
