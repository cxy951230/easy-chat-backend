/*
 * Copyright (c) 2019 InnovativE SoftwAre TechnoloGy DeveLopment CentEr, EAGLE-Lab.
 */

package com.cxy.handler;

import com.cxy.entity.common.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ResponseEntity res = ResponseEntity.error(String.valueOf(HttpStatus.FORBIDDEN.value()), "没有访问权限！");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(res));
    }
}
