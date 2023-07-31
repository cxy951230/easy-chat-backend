package com.cxy.http;

import org.springframework.security.web.util.OnCommittedResponseWrapper;

import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseWrapper extends OnCommittedResponseWrapper {

    HttpServletRequestWrapper request;

    public HttpServletResponseWrapper(HttpServletResponse response,HttpServletRequestWrapper request) {
        super(response);
        this.request = request;
    }

    @Override
    protected void onResponseCommitted() {
        this.request.commitSession();
    }
}
