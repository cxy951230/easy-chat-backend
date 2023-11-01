package com.cxy.handler;

import com.alibaba.fastjson.JSONObject;
import com.cxy.entity.*;
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
        WsResponse response = new WsResponse();
        WebSocketSession session = AbstractWebSocketSessionHandler.webSocketSessionThreadLocal.get();
        UserVo user = (UserVo)session.getAttrs().get("user");
//        response.setMsg("receive msg " + wsRequest.getMsg());
        response.setMsg(wsRequest.getMsg());
//        UserVo userVo = new UserVo();
//        userVo.setUsername(user.getUsername());
        response.getExtra().put("user", user);
        AbstractWebSocketSessionHandler.notifyAllSession(JSONObject.toJSONString(response));
    }
}
