package com.shangqiu.service;

import com.shangqiu.persistence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by dev-101 on 2017/7/3.
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public Map queryUserById(Map map){
      return  userMapper.queryUserById(map);
    }

}
