package com.cxy.handler;

import com.cxy.entity.common.AuthenticationUser;
import com.cxy.entity.common.ResponseEntity;
import com.cxy.entity.enums.CookieTypeEnum;
import com.cxy.utils.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AuthenticationUser userDetails = (AuthenticationUser) authentication.getPrincipal();
        //把sessionId加入cookie
        CookieUtil.setCookie(httpServletResponse, CookieTypeEnum.SESSIONID.getType(), httpServletRequest.getSession().getId());
        CookieUtil.setCookie(httpServletResponse, CookieTypeEnum.USERID.getType(), userDetails.getUser().getId().toString());
//        CookieUtil.setCookie(httpServletResponse, "Secure", "true");
//        CookieUtil.setCookie(httpServletResponse, "SameSite", "None");


        Map<String, Object> result = new HashMap<>();
//        result.put("goUrl", request.getParameter("goUrl"));
        result.put("message", "登录成功！");

        ResponseEntity res = ResponseEntity.success("success",result);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(res));
    }
}
