package com.cxy.http;

import com.cxy.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

@Component
public class SessionRepository {

    private static final String springSecurityContextKey= "SPRING_SECURITY_CONTEXT";
    @Autowired
    private JedisUtil jedisUtil;

    public void setSession(String sessionId, Session session){
        try{
            //这行如果放开，remenberFilter会重新校验登录，然后被successHandler处理，不会走到cookieFilter
            //也不会走到权限校验
//            session.removeAttribute(springSecurityContextKey);
            session.setServletContext(null);
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(session);
            out.close();
            // 将字节流转换为字符串并打印
            byte[] bytes = byteOut.toByteArray();
            System.out.println("Serialized Content:");
//            String s = new String(bytes, StandardCharsets.UTF_8);
            jedisUtil.set(sessionId, Base64.getEncoder().encodeToString(bytes));

//            ByteArrayInputStream in2 = new ByteArrayInputStream(serializedString.getBytes());
//            ObjectInputStream out2 = new ObjectInputStream(in2);
//            Session session2 = (Session) out2.readObject();
//            in2.close();
//            out2.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Session getSessionById(String sessionId){
        try {
            String base64String = (String) jedisUtil.get(sessionId);
            System.out.println(base64String);
            if (base64String == null) {
                return null;
            }
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(base64String));
            ObjectInputStream out = new ObjectInputStream(in);
            Session session = (Session) out.readObject();
            in.close();
            out.close();
//        Session session = (Session) obj;
//        Object context = session.getAttribute(springSecurityContextKey);
//        if(context != null){
//            JSONObject jsonObject = (JSONObject)context;
//            SecurityContextImpl securityContext = JSONObject.parseObject(jsonObject.toJSONString(), SecurityContextImpl.class);
//            session.setAttribute(springSecurityContextKey, securityContext);
//        }
            return session;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
