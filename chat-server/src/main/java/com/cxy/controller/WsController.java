package com.cxy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class WsController {

    @RequestMapping("")
    public void info(){
        System.out.println(1);
    }
}
