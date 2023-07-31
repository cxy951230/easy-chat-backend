package com.cxy.handler;

import com.alibaba.fastjson.JSONObject;
import com.cxy.entity.WsRequest;
import com.cxy.entity.WsResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ChannelHandler.Sharable
public class DefaultServerHandler extends AbstractServerHandler{
    private AbstractWebSocketSessionHandler webSocketSessionHandler;

    @Override
    public void ping(ChannelHandlerContext ctx, WsRequest wsRequest) {

    }

    @Override
    public void msg(ChannelHandlerContext ctx, WsRequest wsRequest) {
        System.out.println(wsRequest.getMsg());
        WsResponse response = new WsResponse();
        response.setMsg("res");
        response.setCode(33);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(response)));
    }
}
