package com.cxy.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpObject;

import java.util.Arrays;

public abstract class AbstractCookieCheckHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final HttpObject httpObject = (HttpObject) msg;
        final HttpMessage req = (HttpMessage) httpObject;
        HttpHeaders headers = req.headers();
        headers.entries().forEach(e->{
            if("Cookie".equals(e.getKey())){
                String[] cookies = e.getValue().split(";");
                Arrays.stream(cookies).forEach(cookie->{
                    checkCookie(cookie);
                });
            }
        });
        ctx.pipeline().remove(this);
        super.channelRead(ctx, msg);
    }

    public abstract Boolean checkCookie(String cookie);
}
