package com.cxy.handler;

import com.cxy.entity.WebSocketSession;
import com.cxy.utils.JedisUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ChannelHandler.Sharable
public class DefaultWebSocketSessionHandler extends AbstractWebSocketSessionHandler implements InitializingBean {

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


    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(()->{
            for(;;){
                AbstractWebSocketSessionHandler.SESSION_MAP.values().stream().forEach(e->{
                    if(isExpire(e)){
                        ChannelFuture f = e.getChannel().writeAndFlush(new TextWebSocketFrame(AbstractWebSocketSessionHandler.EXPIRE_MSG));
                        f.addListener(ChannelFutureListener.CLOSE);
                        SESSION_MAP.remove(e.getId());
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
