package com.cxy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cxy.entity.Permission;
import com.cxy.entity.RolePermission;
import com.cxy.entity.UserRole;
import com.cxy.mapper.PermissionMapper;
import com.cxy.mapper.RolePermissionMapper;
import com.cxy.mapper.UserMapper;
import com.cxy.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    public List<Permission> getAllPermissions(){
        QueryWrapper<Permission> permissionQuery = new QueryWrapper<>();
        List<Permission> permissions = permissionMapper.selectList(permissionQuery);
        return permissions;
    }

    public List<Permission> getPermissionsByUserId(Integer userId){
        QueryWrapper<UserRole> roleQuery = new QueryWrapper();
        roleQuery.eq("user_id", userId);
        List<UserRole> userRoles = userRoleMapper.selectList(roleQuery);
        if(userRoles.size() == 0){
            return new ArrayList<>();
        }
        QueryWrapper<RolePermission> rolePermissionQuery = new QueryWrapper<>();
        rolePermissionQuery.in("role_id", userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
        List<RolePermission> rolePermissions = rolePermissionMapper.selectList(rolePermissionQuery);
        QueryWrapper<Permission> permissionQuery = new QueryWrapper<>();
        permissionQuery.in("id", rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList()));
        List<Permission> permissions = permissionMapper.selectList(permissionQuery);
        return permissions;
    }
}
