package com.cxy.controller;

import com.cxy.entity.User;
import com.cxy.entity.common.ResponseEntity;
import com.cxy.mapper.UserMapper;
import com.cxy.service.UserService;
import com.cxy.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    public UserMapper userMapper;

    @Autowired
    private JedisUtil jedisUtil;

    @GetMapping("/test")
    public void test(){
        System.out.println("test");
        User user = new User();
        user.setUsername("cxy");
        User user1 = userMapper.selectById(1);
        System.out.println(user1.getUsername());
        jedisUtil.set("ks",11);
    }

    @GetMapping("/test2")
    public void test2(){
        System.out.println("test2");
    }

    @PostMapping("/login")
    public void login(String username,String password){
        System.out.println("login");
        System.out.println(username);
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody User user){
        return userService.signUp(user);
    }
}
