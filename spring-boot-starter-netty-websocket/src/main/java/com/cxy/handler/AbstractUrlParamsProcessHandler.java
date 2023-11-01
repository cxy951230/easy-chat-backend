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

public abstract class AbstractUrlParamsProcessHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(AbstractUrlParamsProcessHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try{
            if (!(msg instanceof HttpObject)){
                ctx.pipeline().remove(this);
                ctx.fireChannelRead(msg);
                return;
            }
            final HttpObject httpObject = (HttpObject) msg;
            final HttpRequest req = (HttpRequest) httpObject;
            String uri = req.uri();
            HashMap<String, String> paramsMap = new HashMap();
            if(uri.contains("?")){
                String paramsStr = uri.substring(uri.indexOf("?") + 1);
                String[] params = paramsStr.split("&");
                Arrays.stream(params).forEach(e->{
                    String[] param = e.split("=");
                    paramsMap.put(param[0], param[1]);
                });
            }
            if(checkParams(paramsMap)){
                super.channelRead(ctx, msg);
            }else{
                logger.info("URL PARAMS CHECK ERROR");
                HttpUtil.sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN, ctx.alloc().buffer(0)));
                return;
            }

//            final HttpObject httpObject = (HttpObject) msg;
//            final HttpMessage req = (HttpMessage) httpObject;
//
//
//            HttpHeaders headers = req.headers();
//            for (Map.Entry<String, String> entry : headers.entries()) {
//                if("Cookie".equals(entry.getKey())){
//                    String[] cookies = entry.getValue().split(";");
//                    for (String cookie : cookies) {
//                        if(!checkCookie(cookie)){
//                            logger.error("cookie check err, user need login");
//                            return;
//                        }
//                    }
//                }
//            }
//            super.channelRead(ctx, msg);
        }catch (Exception e){
            logger.error("check url params err", e);
        }finally {
            ctx.pipeline().remove(this);
            finishCheck(ctx);
        }
    }


    public abstract void finishCheck(ChannelHandlerContext ctx);

    public abstract Boolean checkParams(HashMap<String, String> paramsMap);

}
