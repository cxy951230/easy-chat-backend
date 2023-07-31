package com.cxy.http.filter;

import com.cxy.http.HttpServletRequestWrapper;
import com.cxy.http.HttpServletResponseWrapper;
import com.cxy.http.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Integer.MIN_VALUE + 1)
public class SessionFilter extends OncePerRequestFilter {

    public static SessionRepository sessionRepository;

    @Autowired
    public void setSessionRepository(SessionRepository sessionRepository){
        this.sessionRepository = sessionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequestWrapper request = new HttpServletRequestWrapper(httpServletRequest, httpServletResponse);
        HttpServletResponseWrapper response = new HttpServletResponseWrapper(httpServletResponse, request);
        try{
            filterChain.doFilter(request,response);
        }finally {
            request.commitSession();
        }
    }
}
