package com.everr.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.everr.reggie_take_out.entiy.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
