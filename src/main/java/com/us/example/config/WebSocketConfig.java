package com.us.example.config;

import com.us.example.interceptor.HttpSessionIdHandshakeInterceptor;
import com.us.example.interceptor.PresenceChannelInterceptor;
import com.us.example.interceptor.SessionAuthHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
/**
 * 通过EnableWebSocketMessageBroker
 * 开启使用STOMP协议来传输基于代理(message broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
 */
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //endPoint 注册协议节点,并映射指定的URl

        //注册一个Stomp 协议的endpoint,并指定 SockJS协议
        registry.addEndpoint("/endpointWisely").withSockJS();

        //注册一个名字为"endpointChat" 的endpoint,并指定 SockJS协议。   点对点-用
        registry.addEndpoint("/endpointChat").withSockJS()
            .setInterceptors(httpSessionIdHandshakeInterceptor());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
       /* registration.taskExecutor().corePoolSize(4) //设置消息输入通道的线程池线程数
                .maxPoolSize(8)//最大线程数
                .keepAliveSeconds(60);//线程活动时间*/
        registration.interceptors(presenceChannelInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理(message broker)
        //广播式应配置一个/topic 消息代理
        registry.enableSimpleBroker("/topic");

        //点对点式增加一个/queue 消息代理
        registry.enableSimpleBroker("/queue", "/topic");

    }

    /**
     * 消息传输参数配置
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(8192) //设置消息字节数大小
                .setSendBufferSizeLimit(8192)//设置消息缓存大小
                .setSendTimeLimit(10000); //设置消息发送时间限制毫秒
    }

    @Bean
    public HttpSessionIdHandshakeInterceptor httpSessionIdHandshakeInterceptor() {
        return new HttpSessionIdHandshakeInterceptor();
    }

    @Bean
    public PresenceChannelInterceptor presenceChannelInterceptor() {
        return new PresenceChannelInterceptor();
    }

    @Bean
    public SessionAuthHandshakeInterceptor sessionAuthHandshakeInterceptor() {
        return new SessionAuthHandshakeInterceptor();
    }

}
