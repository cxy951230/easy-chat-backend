package com.cxy.handler;

import com.cxy.entity.WsRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

@ChannelHandler.Sharable
public class ServerHandler extends AbstractServerHandler{
    @Override
    public void ping(ChannelHandlerContext ctx, WsRequest wsRequest) {
        System.out.println("ping...");
    }

    @Override
    public void msg(ChannelHandlerContext ctx, WsRequest wsRequest) {
        System.out.println(wsRequest.getMsg());
    }
}
