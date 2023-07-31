package com.cxy.service.impl;

import com.cxy.entity.User;
import com.cxy.entity.common.ResponseEntity;
import com.cxy.mapper.UserMapper;
import com.cxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseEntity signUp(User user) {
        userMapper.insert(user);
        return ResponseEntity.success();
    }
}
