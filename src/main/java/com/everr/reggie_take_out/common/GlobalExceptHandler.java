package com.everr.reggie_take_out.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * description :
 *
 * @author: everr
 * @date: 2023/03/19 19:30
 **/

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptHandler {
    /**
     * 异常处理方法
     */
@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){

    log.error(ex.getMessage());


    if (ex.getMessage().contains("Duplicate entry")){
        String[] split = ex.getMessage().split(" ");
        String msg = split[2] + "已存在";
        return R.error(msg);

    }

    return R.error("未知错误");
}

}
