package com.cxy.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;

public class HttpUtil {
//    ByteBuf content = WebSocketServerIndexPage.getContent(webSocketLocation);
//    FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), OK, content);
//
//            res.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
//            io.netty.handler.codec.http.HttpUtil.setContentLength(res, content.readableBytes());
//
//    sendHttpResponse(ctx, req, res);

    public static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        HttpResponseStatus responseStatus = res.status();
        if (responseStatus.code() != 200) {
            ByteBufUtil.writeUtf8(res.content(), responseStatus.toString());
            io.netty.handler.codec.http.HttpUtil.setContentLength(res, res.content().readableBytes());
        }
        // Send the response and close the connection if necessary.
        boolean keepAlive = io.netty.handler.codec.http.HttpUtil.isKeepAlive(req) && responseStatus.code() == 200;
        io.netty.handler.codec.http.HttpUtil.setKeepAlive(res, keepAlive);
        ChannelFuture future = ctx.writeAndFlush(res);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

}
