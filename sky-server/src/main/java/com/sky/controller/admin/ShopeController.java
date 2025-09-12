package com.sky.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sky.result.Result;

import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@RestController("adminShopeController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopeController {
     
    public static final String key = "SHOP_STATUS";

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @PutMapping("/status/{status}")
    @ApiOperation("设置店铺营业状态")
    public Result<String> setStatus(@PathVariable Integer status){
        log.info("设置店铺营业状态为:{}",status==1?"营业中":"打烊中");

        redisTemplate.opsForValue().set("SHOP_STATUS",status);
        return Result.success();
    }
    @GetMapping("/status")
    public Result<Integer> getStatus(){

        Integer status = (Integer) redisTemplate.opsForValue().get(key);
        log.info("获取店铺营业状态为:{}",status==1?"营业中":"打烊中");
       
        return Result.success(status);
    }
    
}
