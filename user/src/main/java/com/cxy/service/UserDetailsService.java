package com.cxy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxy.entity.Permission;
import com.cxy.entity.User;
import com.cxy.entity.common.AuthenticationUser;
import com.cxy.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(user == null){
            throw new RuntimeException("用户名不存在");
        }
        List<Permission> permissions = permissionService.getPermissionsByUserId(user.getId());

        permissions.forEach(e->{
            String name = e.getName();
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(name);
            grantedAuthorities.add(grantedAuthority);
        });

        AuthenticationUser authenticationUser = new AuthenticationUser(user.getUsername(),
                user.getPassword(), grantedAuthorities);
        authenticationUser.setUser(user);
        return authenticationUser;
    }
}
