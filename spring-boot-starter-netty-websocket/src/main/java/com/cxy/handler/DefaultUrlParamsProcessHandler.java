package com.cxy.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
//@NoArgsConstructor
@ChannelHandler.Sharable
public class DefaultUrlParamsProcessHandler extends AbstractUrlParamsProcessHandler {

//    @Override
//    public Boolean checkCookie(String cookie) {
////        String[] arrays = cookie.trim().split("=");
////        if(arrays.length == 2 && arrays[0].equals("sessionId")){
////            Session session = sessionRepository.getSessionById(arrays[1]);
////            System.out.println(111);
////        }
//        return true;
//    }

    @Override
    public void finishCheck(ChannelHandlerContext ctx) {

    }

    @Override
    public Boolean checkParams(HashMap paramsMap) {
        return true;
    }
}
