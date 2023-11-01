package com.cxy.handler;

import com.cxy.entity.WebSocketSession;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public abstract class AbstractWebSocketSessionHandler extends ChannelInboundHandlerAdapter {
    Logger logger = LoggerFactory.getLogger(AbstractWebSocketSessionHandler.class);
    public static final AttributeKey<WebSocketSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");
    protected static Map<String, WebSocketSession> SESSION_MAP = new HashMap<>();
    public static ThreadLocal<WebSocketSession> webSocketSessionThreadLocal = new ThreadLocal<>();
    protected static String EXPIRE_MSG = "SESSION IS EXPIRE";

    //ws握手后触发监听事件，构建session
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete event = (WebSocketServerProtocolHandler.HandshakeComplete)evt;
            WebSocketSession session = ctx.channel().attr(SESSION_KEY).get();
            if(session == null){
                session = new WebSocketSession(ctx.channel());
                session.set("aa","ff");
                ctx.channel().attr(SESSION_KEY).set(session);
                SESSION_MAP.put(session.getId(), session);
                saveSession(session);//实现类持久化session
            }else {

            }
        }
    }

    //检查session过期
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        WebSocketSession session = ctx.channel().attr(SESSION_KEY).get();
        if(session != null && !isExpire(session)){
            try{
                webSocketSessionThreadLocal.set(session);
                super.channelRead(ctx, msg);
            }catch (Exception e){

            }finally {
                webSocketSessionThreadLocal.remove();
            }
        }else {
            logger.error(EXPIRE_MSG);
            ChannelFuture f = ctx.channel().writeAndFlush(new TextWebSocketFrame(EXPIRE_MSG));
            f.addListener(ChannelFutureListener.CLOSE);
            SESSION_MAP.remove(session != null ? session.getId() : "");
        }
    }

    public void heartBeat(ChannelHandlerContext ctx){
        WebSocketSession session = ctx.channel().attr(SESSION_KEY).get();
        resetExpire(session);
    }

    public void removeSession(String sessionId){
        SESSION_MAP.remove(sessionId);
    }

    //广播消息
    public static void notifyAllSession(String msg){
        SESSION_MAP.values().forEach(e->{
            System.out.println(">>>>>");
            System.out.println(e.getId());
            System.out.println(AbstractWebSocketSessionHandler.webSocketSessionThreadLocal.get().getId());
            if(!e.getId().equals(AbstractWebSocketSessionHandler.webSocketSessionThreadLocal.get().getId())){
                Channel channel = e.getChannel();
                channel.writeAndFlush(new TextWebSocketFrame(msg));
            }
        });
    }

    public abstract void saveSession(WebSocketSession session);

    public abstract Boolean isExpire(WebSocketSession session);

    public abstract void resetExpire(WebSocketSession session);


}