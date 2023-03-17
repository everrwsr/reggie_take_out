package com.everr.reggie_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.everr.reggie_take_out.entiy.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: hanbing
 * Date: 2023-03-17
 * Time: 17:06
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
