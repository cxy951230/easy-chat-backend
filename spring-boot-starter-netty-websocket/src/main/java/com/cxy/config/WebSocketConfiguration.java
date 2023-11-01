package com.cxy.config;

import com.cxy.handler.*;
import com.cxy.utils.JedisUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfiguration {

    @Value("${websocket.port}")
    private Integer PORT;
    @Value("${websocket.uri}")
    private String WEBSOCKET_PATH;
    @Value("${websocket.expire}")
    private Integer expire;

    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    public AbstractWebSocketSessionHandler webSocketSessionHandler;

    @Autowired
    private AbstractServerHandler serverHandler;

    @Autowired
    private AbstractUrlParamsProcessHandler paramsProcessHandler;

    @Bean
    public ServerBootstrap serverBootstrap(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>(){

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new WebSocketServerCompressionHandler());
                            pipeline.addLast(paramsProcessHandler);
                            // 构建 CorsConfig，设置允许的跨域请求信息
                            CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();
                            // 添加 CorsHandler 处理器
                            pipeline.addLast(new CorsHandler(corsConfig));
                            pipeline.addLast(new PathCheckHandler(WEBSOCKET_PATH));
//                            pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
                            pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true, 65536, false, true, 1000L));
                            pipeline.addLast(webSocketSessionHandler);
                            pipeline.addLast(serverHandler);
                        }

                    });
            ChannelFuture sync = serverBootstrap.bind(PORT).sync();

//            InetSocketAddress addr = (InetSocketAddress) sync.channel().localAddress();
//            System.out.println(addr.getPort());;
//            Channel ch = serverBootstrap.bind(PORT).sync().channel();

//            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }
        return serverBootstrap;
    }

    @Bean
    @ConditionalOnMissingBean(AbstractUrlParamsProcessHandler.class)
    public AbstractUrlParamsProcessHandler defaultUrlParamsProcessHandler(){
        return new DefaultUrlParamsProcessHandler();
    }

    @Bean
    @ConditionalOnMissingBean(AbstractWebSocketSessionHandler.class)
    public AbstractWebSocketSessionHandler defaultWebSocketSessionHandler(){
        return new DefaultWebSocketSessionHandler(jedisUtil, expire);
    }

    @Bean
    @ConditionalOnMissingBean(AbstractServerHandler.class)
    public AbstractServerHandler defaultServerHandler(){
        return new DefaultServerHandler(webSocketSessionHandler);
    }
}
