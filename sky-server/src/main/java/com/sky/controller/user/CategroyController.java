package com.sky.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.sky.result.Result;
import com.sky.entity.Category;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.sky.service.CategoryService;
@RestController
@RequestMapping("/user/category")
@Api(tags = "C端-分类接口")
public class CategroyController {
  
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    @ApiOperation(value = "查询分类")
    public Result<List<Category>> list(Integer type) {
        return Result.success(categoryService.list(type));
    }
}
