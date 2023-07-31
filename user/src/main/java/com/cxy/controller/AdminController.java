package com.cxy.controller;

import com.cxy.mapper.UserMapper;
import com.cxy.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    public UserMapper userMapper;

    @Autowired
    private JedisUtil jedisUtil;

    @GetMapping("/t1")
    public void test(HttpServletRequest request){
        System.out.println("admin111");
    }

    @GetMapping("/t2")
    public void test2(HttpServletRequest request){
        System.out.println("admin2222");
    }
}
