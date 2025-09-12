package com.sky.service.impl;

import com.sky.service.ShoppingCartService;
import com.sky.dto.ShoppingCartDTO;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.List;
import com.sky.context.BaseContext;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车,商品信息为:{}", shoppingCartDTO);
         
        //查询当前用户是否存在购物车
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        //如果存在，只需要更新数量，不需要重新插入
        if (list!=null && list.size() > 0) {
             ShoppingCart cart = list.get(0);
             cart.setNumber(cart.getNumber() + 1);//update shopping_cart set number= ? where   id=?
             shoppingCartMapper.update(cart);
        }else{
                //如果不存在，则插入购物车

                //判断本次添加的是菜品还是套餐
                Long dishId = shoppingCartDTO.getDishId();
               if(dishId!=null){
                //添加菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

               }else{
                //添加套餐
                 Long setmealId = shoppingCartDTO.getSetmealId();
                 Setmeal setmeal = setmealMapper.getById(setmealId);
                 shoppingCart.setName(setmeal.getName());
                 shoppingCart.setImage(setmeal.getImage());
                 shoppingCart.setAmount(setmeal.getPrice());

                
               }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
            }
        
        shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public List<ShoppingCart> listShoppingCart() {
        //利用BaseContext获取当前用户id
        Long userId = BaseContext.getCurrentId();
        //创建购物车对象
        ShoppingCart shoppingCart = new ShoppingCart();
        //shoppingCart中的结构是userId，dishId，setmealId，dishFlavor
        shoppingCart.setUserId(userId);
        //调用mapper层查询购物车列表
        //shoppingCartMapper接收shoppingCart对象，返回购物车列表
        return shoppingCartMapper.list(shoppingCart);
    }

}
