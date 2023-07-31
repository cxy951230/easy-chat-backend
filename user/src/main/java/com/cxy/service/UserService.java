package com.cxy.service;

import com.cxy.entity.User;
import com.cxy.entity.common.ResponseEntity;

public interface UserService {
    ResponseEntity signUp(User user);
}
