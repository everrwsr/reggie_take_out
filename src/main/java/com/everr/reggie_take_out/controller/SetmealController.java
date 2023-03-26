package com.everr.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.everr.reggie_take_out.common.R;
import com.everr.reggie_take_out.dto.SetmealDto;
import com.everr.reggie_take_out.entiy.Category;
import com.everr.reggie_take_out.entiy.Setmeal;
import com.everr.reggie_take_out.service.CategoryService;
import com.everr.reggie_take_out.service.SetmealDishService;
import com.everr.reggie_take_out.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 套餐的起售与停售
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> onOrClose(@PathVariable Integer status, Long[] ids) {
        log.info("setmeal====>{},status==>{}" + ids + "====>" + status);
        for (int i = 0; i < ids.length; i++) {
            // 获取菜品
            Setmeal setmeal = setmealService.getById(ids[i]);
            setmeal.setStatus(status);
            // 修改状态
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功");
    }

    /**
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功！");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();


        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件
        queryWrapper.like(name != null, Setmeal::getName, name);
        // 添加 排序条件 ，真不想写啊，其实也没有写的理由了2023.03.29
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        // 执行查询操作
        setmealService.page(pageInfo, queryWrapper);


        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();


        List<SetmealDto> list = records.stream().map((item) -> {

            SetmealDto setmealDto = new SetmealDto();
            // 对象拷贝
            BeanUtils.copyProperties(item, setmealDto);
            // 分类id
            Long categoryId = item.getCategoryId();
            // 根据分类ID查询分类对象
            Category category = categoryService.getById(categoryId);

            if (categoryId != null) {
                // 分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);

            }
            return setmealDto;

        }).collect(Collectors.toList());


        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);


        setmealService.removeWithDish(ids);

        return R.success("套餐删除成功");

    }

    /**
     * 根据套件查询数据
     *
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
       queryWrapper.eq(setmeal.getStatus() != null , Setmeal::getStatus,setmeal.getStatus());
       queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);


        return R.success(list);

    }
}
