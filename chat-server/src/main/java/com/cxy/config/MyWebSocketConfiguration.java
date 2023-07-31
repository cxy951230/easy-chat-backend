package com.cxy.config;

import com.cxy.handler.AbstractServerHandler;
import com.cxy.handler.ServerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyWebSocketConfiguration {

    @Bean
    public AbstractServerHandler serverHandler(){
        return new ServerHandler();
    }
}
