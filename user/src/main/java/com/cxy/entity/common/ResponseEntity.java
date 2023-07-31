package com.cxy.entity.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntity {
    private Integer status;
    private Object data;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    public ResponseEntity(Integer status, String msg, Object content) {

        timestamp = new Date();

        this.status = status;
        data = content;
        message = msg;
    }

    public static ResponseEntity success() {
        ResponseEntity r = new ResponseEntity();
        r.setStatus(0);
        r.setMessage("success");
        r.setData(null);
        return r;
    }

    public static ResponseEntity success(String msg, Object data) {
        ResponseEntity r = new ResponseEntity();
        r.setStatus(0);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static ResponseEntity error(String msg, Object data) {
        ResponseEntity r = new ResponseEntity();
        r.setStatus(1);
        r.setMessage(msg);
        r.setData(data);
        return r;
    }

    public static ResponseEntity error(String msg) {
        ResponseEntity r = new ResponseEntity();
        r.setStatus(1);
        r.setMessage(msg);
        return r;
    }
}
