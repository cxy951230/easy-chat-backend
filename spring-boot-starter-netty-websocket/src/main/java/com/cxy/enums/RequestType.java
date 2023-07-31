package com.cxy.enums;

import java.util.Arrays;
import java.util.Optional;

public enum RequestType {
    PING("ping", 1),
    MSG("msg", 2);

    private String msgType;
    private Integer code;
    private RequestType(String msgType,Integer code){
        this.msgType = msgType;
        this.code = code;
    }

    public String getMsgType(){
        return this.msgType;
    }

    public Integer getCode(){
        return this.code;
    }

    public static RequestType getByCode(Integer code){
        Optional<RequestType> res= Arrays.stream(RequestType.values()).filter(e->{
            return e.getCode().equals(code);
        }).findFirst();
        RequestType requestType = res.get();
        return requestType;
    }
}
