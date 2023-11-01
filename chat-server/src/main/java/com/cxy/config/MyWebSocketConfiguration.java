package com.cxy.config;

import com.cxy.handler.AbstractServerHandler;
import com.cxy.handler.AbstractUrlParamsProcessHandler;
import com.cxy.handler.ServerHandler;
import com.cxy.handler.UrlParamsProcessHandler;
import com.cxy.http.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyWebSocketConfiguration {

    @Autowired
    private SessionRepository sessionRepository;


    @Bean
    public AbstractServerHandler serverHandler(){
        return new ServerHandler();
    }

    @Bean
    public AbstractUrlParamsProcessHandler urlParamsProcessHandler(){
        return new UrlParamsProcessHandler(sessionRepository);
    }
}
