package com.cxy.filter;

import com.cxy.entity.common.AuthenticationUser;
import com.cxy.entity.common.ResponseEntity;
import com.cxy.entity.enums.CookieTypeEnum;
import com.cxy.utils.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CookieCheckFilter  extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //根据sessionid找到缓存的context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {

            Cookie cookie = CookieUtil.getCookie(request, CookieTypeEnum.USERID.getType());

            if (cookie != null) {
                String localUserId = ((AuthenticationUser) authentication.getPrincipal()).getUser().getId().toString();
                if (!cookie.getValue().equals(localUserId)) {
                    ResponseEntity result = ResponseEntity.error("Cookie记录用户不是当前用户");
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    ObjectMapper objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(result));
                    return;
                }
            }else {
                ResponseEntity result = ResponseEntity.error("用户id为空");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(result));
                return;
            }
        }else{
//            ResponseEntity result = ResponseEntity.error("用户未登录");
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            ObjectMapper objectMapper = new ObjectMapper();
//            response.getWriter().write(objectMapper.writeValueAsString(result));
//            return;
        }

        filterChain.doFilter(request, response);
    }
}
