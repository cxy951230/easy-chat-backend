/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.cxy.handler;

import com.alibaba.fastjson.JSONObject;
import com.cxy.entity.WsRequest;
import com.cxy.enums.RequestType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;


public abstract class AbstractServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Autowired
    private AbstractWebSocketSessionHandler webSocketSessionHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled

        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).text();
            WsRequest wsRequest = JSONObject.parseObject(request, WsRequest.class);
            RequestType code = RequestType.getByCode(wsRequest.getCode());

            ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
            switch (code){
                case PING:
                    webSocketSessionHandler.heartBeat(ctx);
                    ping(ctx, wsRequest);
                    break;
                case MSG:
                    webSocketSessionHandler.heartBeat(ctx);
                    msg(ctx, wsRequest);
                    break;
                default:
                    System.out.println("eeeeee");
            }
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    public abstract void ping(ChannelHandlerContext ctx, WsRequest wsRequest);

    public abstract void msg(ChannelHandlerContext ctx, WsRequest wsRequest);
}
