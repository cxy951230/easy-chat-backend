package com.cxy.entity;

import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WebSocketSession {

    private String id;

    private Map attrs;

    private Channel channel;

    public WebSocketSession(Channel channel){
        this.id = UUID.randomUUID().toString();
        this.attrs = new HashMap();
        this.channel = channel;
    }

    public Object set(String key, Object obj){
        this.attrs.put(key, obj);
        return obj;
    }

    public Object get(String key){
        return this.attrs.get(key);
    }

    public Object remove(String key){
        return this.attrs.remove(key);
    }
}
