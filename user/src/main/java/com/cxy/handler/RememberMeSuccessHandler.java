package com.cxy.handler;

import com.cxy.entity.common.AuthenticationUser;
import com.cxy.entity.enums.CookieTypeEnum;
import com.cxy.utils.CookieUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RememberMeSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AuthenticationUser userDetails = (AuthenticationUser) authentication.getPrincipal();
        CookieUtil.setCookie(httpServletResponse, CookieTypeEnum.USERID.getType(), userDetails.getUser().getId().toString());
    }
}
