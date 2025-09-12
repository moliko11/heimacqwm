package com.sky.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import com.sky.entity.ShoppingCart;
import java.util.List;
@Mapper
public interface ShoppingCartMapper {
    /*
     * 动态查询购物车
     * 对于mapper方法是怎么将对象进行解析的我还不理解
     * UNkown 先放一个记号，后面再补
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /*
     * 更新购物车
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart shoppingCart);

    /*
     * 插入购物车
     */
    @Insert("insert into shopping_cart (name,user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) values (#{name}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{image}, #{createTime})")
    void insert(ShoppingCart shoppingCart);
    
}
