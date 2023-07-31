package com.cxy.http;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.io.Serializable;
import java.util.*;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Session implements HttpSession, Serializable {
    private static final long serialVersionUID = 1L;

    public Session(ServletContext servletContext){
        this.servletContext = servletContext;
    }
    private String id;
    private long last;
    private long lastAccessedTime;
    private long creationTime;
    private int maxInactiveInterval;

    private ServletContext servletContext;
    private Map<String,Object> attrs = new HashMap<>();
    private boolean isNew = true;
    private boolean invalidated = false;

    @Override
    public long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setMaxInactiveInterval(int i) {
        this.maxInactiveInterval = i;
    }

    @Override
    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return attrs.get(s);
    }

    @Override
    public Object getValue(String s) {
        return getAttribute(s);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(this.attrs.keySet());
    }

    @Override
    public String[] getValueNames() {
        return attrs.keySet().toArray(new String[0]);
    }

    @Override
    public void setAttribute(String s, Object o) {
        this.attrs.put(s,o);
    }

    @Override
    public void putValue(String s, Object o) {
        setAttribute(s,o);
    }

    @Override
    public void removeAttribute(String s) {
        this.attrs.remove(s);
    }

    @Override
    public void removeValue(String s) {
        this.removeAttribute(s);
    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    public void markNotNew(){
        this.isNew = false;
    }
}
