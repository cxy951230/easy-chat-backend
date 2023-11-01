package com.cxy.handler;

import com.cxy.entity.User;
import com.cxy.entity.UserVo;
import com.cxy.entity.WebSocketSession;
import com.cxy.http.Session;
import com.cxy.http.SessionRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ChannelHandler.Sharable
public class UrlParamsProcessHandler extends AbstractUrlParamsProcessHandler {
    private ThreadLocal<User> threadLocal = new ThreadLocal<>();
    private SessionRepository sessionRepository;

    public UrlParamsProcessHandler(SessionRepository sessionRepository){
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void finishCheck(ChannelHandlerContext ctx) {
        WebSocketSession session = ctx.channel().attr(AbstractWebSocketSessionHandler.SESSION_KEY).get();
        User user = threadLocal.get();
        UserVo userVo = new UserVo();
        userVo.setUserId(user.getId());
        userVo.setUsername(user.getUsername());
        session.getAttrs().put("user",userVo);
        threadLocal.remove();
    }

    @Override
    public Boolean checkParams(HashMap<String, String> paramsMap) {
        String sid = paramsMap.get("sessionId");
        if(!StringUtils.isEmpty(sid)){
            Session session = sessionRepository.getSessionById(sid);
            if(session == null){
                return false;
            }else {
                User user = (User) session.getAttribute("user");
                threadLocal.set(user);
            }
        }else{
            return false;
        }
        return true;
    }


}
