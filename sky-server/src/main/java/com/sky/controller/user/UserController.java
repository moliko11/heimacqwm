package com.sky.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import com.sky.result.Result;
import com.sky.vo.UserLoginVO;
import com.sky.dto.UserLoginDTO;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sky.service.Usersevice;
import com.sky.utils.JwtUtil;

import io.swagger.annotations.ApiOperation;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.constant.JwtClaimsConstant;
@RestController
@RequestMapping("/user/user")
@Api(tags = "c端用户相关接口")
@Slf4j
public class UserController {
    @Autowired
    private Usersevice userService;

    @Autowired
    private JwtProperties jwtProperties;


    @PostMapping("/login")
    @ApiOperation(value = "微信用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {

        log.info("微信用户登录：{}", userLoginDTO);
        User user = userService.wxlogin(userLoginDTO);
        //这一步以及完成用户登录，接下来生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        //后面的这些操作就只是为了生成jwt令牌
        //jwt令牌的生成和后端的方式有些不一样
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

  

}
