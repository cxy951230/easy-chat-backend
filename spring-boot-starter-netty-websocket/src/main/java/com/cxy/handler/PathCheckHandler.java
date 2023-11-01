package com.cxy.handler;

import com.cxy.utils.HttpUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
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
            HttpUtil.sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, ctx.alloc().buffer(0)));
            return;
        }
        ctx.pipeline().remove(this);
        ctx.fireChannelRead(msg);
//        if(isInfoReq(req)){
//            logger.info("WEBSOCKET INFO REQUEST");
//            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, OK, ctx.alloc().buffer(0)));
//            return;
//        }
    }


    private boolean isWebSocketPath(HttpRequest req) {
        String uri = req.uri();
        boolean checkStartUri = uri.startsWith(websocketPath);
        boolean checkNextUri = "/".equals(websocketPath) || checkNextUri(uri, websocketPath);
//        return serverConfig.checkStartsWith() ? (checkStartUri && checkNextUri) : uri.equals(websocketPath);
//        boolean res = checkStartUri && checkNextUri;
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

    //    private boolean isInfoReq(HttpRequest req){
//        String infoString = "/info";
//        String uri = req.uri();
//        boolean checkStartUri = uri.startsWith(infoString);
//        boolean checkNextUri = "/".equals(infoString) || checkNextUri(uri, infoString);
////        return serverConfig.checkStartsWith() ? (checkStartUri && checkNextUri) : uri.equals(websocketPath);
//        return checkStartUri && checkNextUri;
//    }
}
