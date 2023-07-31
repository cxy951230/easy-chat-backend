package com.cxy.http;

import com.cxy.entity.enums.CookieTypeEnum;
import com.cxy.http.filter.SessionFilter;
import com.cxy.utils.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {
    private static final String SESSION_KEY = "SESSION_KEY";

    private Session session;

    HttpServletResponse response;
    public HttpServletRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
        super(request);
        this.response = response;
    }

    @Override
    public HttpSession getSession() {
        Session session = (Session)this.getAttribute(SESSION_KEY);
        if(session == null){
            Cookie cookie = CookieUtil.getCookie(this, CookieTypeEnum.SESSIONID.getType());
            if(cookie != null){
                //cookie中有sessionid，则从redis查找
                String sessionId = cookie.getValue();
                session = SessionFilter.sessionRepository.getSessionById(sessionId);
                if (session != null){
                    session.setServletContext(this.getServletContext());
                    this.setAttribute(SESSION_KEY, session);
                    this.session = session;
                    return session;
                }
            }
        }else {
            return session;
        }
        //没有找到session，则创建session对象
        session = new Session(this.getServletContext());
        session.setId(UUID.randomUUID().toString());
        session.markNotNew();
        this.session = session;
        this.setAttribute(SESSION_KEY, session);
        return session;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return getSession();
    }

    public void commitSession(){
        if(this.session != null){
            SessionFilter.sessionRepository.setSession(this.session.getId(), this.session);
        }
    }
}
