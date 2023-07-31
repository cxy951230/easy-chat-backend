package com.cxy.handler;

import com.cxy.entity.WebSocketSession;
import com.cxy.utils.JedisUtil;
import io.netty.channel.ChannelHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ChannelHandler.Sharable
public class DefaultWebSocketSessionHandler extends AbstractWebSocketSessionHandler{

    private JedisUtil jedisUtil;

    private Integer expire;

    @Override
    public void saveSession(WebSocketSession session) {
        jedisUtil.set(session.getId(), "", expire);
    }

    @Override
    public Boolean isExpire(WebSocketSession session) {
        return jedisUtil.get(session.getId()) == null;
    }

    @Override
    public void resetExpire(WebSocketSession session) {
        String id = session.getId();
        jedisUtil.expire(id, expire);
    }
}
