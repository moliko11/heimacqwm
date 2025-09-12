package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.service.Usersevice;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sky.properties.JwtProperties;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.HashMap;
import com.sky.utils.HttpClientUtil;
import com.sky.properties.WeChatProperties;
import com.sky.mapper.UserMapper;
@Service
public class UserseviceImpl implements Usersevice {

    public  static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
  
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
        //微信登录
        String openid = getOpenid(userLoginDTO.getCode());

        if (openid==null) {
            throw new RuntimeException(MessageConstant.LOGIN_FAILED);
        }

        //2. 判断微信用户是否存在，如果存在，则直接返回用户信息，如果不存在，则抛出异常
        User user =userMapper.getByOpenid(openid);

        //如果是新用户，则注册用户
        if (user==null) {
            user = User.builder()
                .openid(openid)
                .createTime(LocalDateTime.now())
                .build();
            userMapper.insert(user);
        }else{
            user = userMapper.getByOpenid(openid);
        }

        //返回用户信息
        return user;
    }
  /*    
   * 根据code获取openid
   * @param code
   * @return
   */
    private String getOpenid(String code){
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", weChatProperties.getAppid());
        paramMap.put("secret", weChatProperties.getSecret());
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        // 1. 调用微信接口服务，获取微信用户的openid
        String result = HttpClientUtil.doGet(WX_LOGIN_URL, paramMap);

        JSONObject jsonObject = JSON.parseObject(result);
        String openid = jsonObject.getString("openid");
        return openid;
    }
    
}
