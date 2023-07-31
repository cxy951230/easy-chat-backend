package com.cxy.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class PathCheckHandler extends ChannelInboundHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(PathCheckHandler.class);

    private String websocketPath;

    public PathCheckHandler(String websocketPath){
        this.websocketPath = websocketPath;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!(msg instanceof HttpObject)){
            ctx.pipeline().remove(this);
            ctx.fireChannelRead(msg);
            return;
        }
        final HttpObject httpObject = (HttpObject) msg;
        final HttpRequest req = (HttpRequest) httpObject;
        if (!isWebSocketPath(req)){
            logger.info("WEBSOCKET PATH ERROR");
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, ctx.alloc().buffer(0)));
            return;
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private boolean isWebSocketPath(HttpRequest req) {
        String uri = req.uri();
        boolean checkStartUri = uri.startsWith(websocketPath);
        boolean checkNextUri = "/".equals(websocketPath) || checkNextUri(uri, websocketPath);
//        return serverConfig.checkStartsWith() ? (checkStartUri && checkNextUri) : uri.equals(websocketPath);
        return checkStartUri && checkNextUri;
    }

    private boolean checkNextUri(String uri, String websocketPath) {
        int len = websocketPath.length();
        if (uri.length() > len) {
            char nextUri = uri.charAt(len);
            return nextUri == '/' || nextUri == '?';
        }
        return true;
    }
}
