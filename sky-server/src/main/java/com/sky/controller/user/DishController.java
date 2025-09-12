package com.sky.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/list")
    @ApiOperation(value = "查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        //查询redis中是否存在菜品数据
        String key="dish_"+categoryId;
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if(list != null && list.size() > 0){
            return Result.success(list);
        }

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);
        //如果不存在，则查询数据库
        list = dishService.listWithFlavor(dish);
        //将查询到的数据缓存到redis中
        redisTemplate.opsForValue().set(key,list);
        return Result.success(list);
    }
}
