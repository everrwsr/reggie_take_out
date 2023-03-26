package com.everr.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.everr.reggie_take_out.common.R;
import com.everr.reggie_take_out.entiy.Category;
import com.everr.reggie_take_out.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    /**
     * @param category
     * @return
     */
    @PostMapping
    public R<String> sava(@RequestBody Category category) {
        log.info("category:{}", category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分类分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        // 分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加排序条件
        queryWrapper.orderByAsc(Category::getSort);

        // 分页查询

        categoryService.page(pageInfo, queryWrapper);


        return R.success(pageInfo);

    }

    /**
     * 根据id删除分类
     *
     * @param id
     * @return
     */


    @DeleteMapping
    public R<String> delete(Long id) {
        log.info("删除分类，id为{}", id);


        categoryService.remove(id);


        return R.success("分类信息删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("分类修改成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        // 条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件选择器
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        // 添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list();

        return R.success(list);
    }
}
