package com.cxy.entity.enums;

public enum CookieTypeEnum {
    USERID("uid", "适残"),
    SESSIONID("sessionId", "cookie中的sessionId");

    private String type;
    private String desc;

    private CookieTypeEnum(String type, String desc){
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
