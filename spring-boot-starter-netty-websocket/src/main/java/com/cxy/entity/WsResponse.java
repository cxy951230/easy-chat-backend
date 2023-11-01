package com.cxy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WsResponse {
    private String msg;
    private Integer code;
    private Map<String,Object> extra = new HashMap<>();
}
