package com.cxy.controller;

import com.cxy.mapper.UserMapper;
import com.cxy.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    public UserMapper userMapper;

    @Autowired
    private JedisUtil jedisUtil;

    @GetMapping("/t1")
    public void test(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.setAttribute("key12", "f111");
        System.out.println(session.getAttribute("key12"));
        session = request.getSession();
    }

    @GetMapping("/t2")
    public void test2(HttpServletRequest request){
        HttpSession session = request.getSession();
        System.out.println(session.getAttribute("key12"));
    }
}
