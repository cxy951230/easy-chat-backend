package com.cxy.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    private static ThreadLocal<Cookie> threadLocal = new ThreadLocal<>();

    public static void setCookie(HttpServletResponse httpServletResponse, String key, String value){
//        Cookie cookie = threadLocal.get();
//        if(cookie == null){
//            cookie = new Cookie();
//
//        }
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    public static Cookie getCookie(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
