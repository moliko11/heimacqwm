package com.sky.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.sky.result.Result;
import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.cache.annotation.Cacheable;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐浏览接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;


     /**
     Cacheable:当使用这个注解的时候，Spring会自动将查询到的数据缓存到redis中
     //先判断redis中是否存在数据，如果存在，则直接返回，如果不存在，则查询数据库
      */
    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询套餐")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId") //key:setmealCache:100
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    @ApiOperation(value = "根据套餐id查询菜品")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id);

        return Result.success(list);
    }
}
