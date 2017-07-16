package com.shangqiu.web.controller;

import com.alibaba.fastjson.JSON;
import com.shangqiu.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by dev-101 on 2017/7/3.
 */
@RequestMapping("/test")
@Controller
public class TestController {

    @Resource
    UserService userService;

    @RequestMapping("/index")
    @ResponseBody
    public String queryUserByOpenId(@RequestParam Map<String,Object> map){
        return JSON.toJSONString(userService.queryUserByOpenId(map));
    }

}
