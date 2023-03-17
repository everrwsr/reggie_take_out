package com.everr.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.everr.reggie_take_out.common.R;
import com.everr.reggie_take_out.entiy.Employee;
import com.everr.reggie_take_out.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * description :
 *
 * @author: everr
 * @date: 2023/03/17 18:06
 **/
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        /*
        1. 将页面提交的密码password进行md5加密处理.
        2. 根据页面提交的用户名username查询数据库
        3、如果没有查询到则返回登录失败结果
        4、窑码比对，如果不一致则返回登失败结果
        5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        6、登录成功，将员工id存入Session并返回登录成功结果
         */

        String password = employee.getPassword();
        password =DigestUtils.md5DigestAsHex(password.getBytes());


        LambdaQueryWrapper<Employee> queryWapper = new LambdaQueryWrapper<>();
        queryWapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWapper);

        if (emp == null) {
            return R.error("登录失败");
        }
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        request.getSession().setAttribute("employee", employee.getId());


        return R.success(emp);
    }

    /**
     * 员工推出
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){

request.getSession().removeAttribute("employee");

        return R.success("退出成功啦");
    }

}
