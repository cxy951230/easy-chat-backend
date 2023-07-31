/*
 * Copyright (c) 2019 InnovativE SoftwAre TechnoloGy DeveLopment CentEr, EAGLE-Lab.
 */

package com.cxy.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author CHEN LEIJIN
 * @description 通过rememberMe登录后在cookie中添加uid和角色信息
 * @date 2019/9/28
 */
public class MyRememberMeAuthenticationFilter extends RememberMeAuthenticationFilter {

    public MyRememberMeAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {

//        AuthenticationUser userDetails = (AuthenticationUser) authResult.getPrincipal();
//        List<Role> roles = userDetails.getUserEx().getRoles();
//        String authorities = "";
//        authorities += roles.get(0).getId();
//        for (int i = 1; i < roles.size(); i++) {
//            authorities += "|" + roles.get(i).getId();
//        }
//        String userId = userDetails.getUserEx().getId().toString();
//
//        response.addCookie(CookieUtils.newCookieBuilder().newAuthorityCookie(authorities).setPath("/").build());
//        response.addCookie(CookieUtils.newCookieBuilder().newUserIdCookie(userId).setPath("/").build());
        System.out.println(11233333);
    }
}
