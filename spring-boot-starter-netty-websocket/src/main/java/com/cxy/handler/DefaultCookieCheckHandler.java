package com.cxy.handler;

import com.cxy.http.Session;
import com.cxy.http.SessionRepository;
import io.netty.channel.ChannelHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ChannelHandler.Sharable
public class DefaultCookieCheckHandler extends AbstractCookieCheckHandler{
    private SessionRepository sessionRepository;

    @Override
    public Boolean checkCookie(String cookie) {
        String[] arrays = cookie.trim().split("=");
        if(arrays.length == 2 && arrays[0].equals("sessionId")){
            Session session = sessionRepository.getSessionById(arrays[1]);
            System.out.println(111);
        }
        return true;
    }
}
