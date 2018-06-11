package com.us.example.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

/**
 * 监听订阅地址的用户
 */
@Component
public class StompSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StompSubscribeEventListener.class);

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        //这里的sessionId对应HttpSessionIdHandshakeInterceptor拦截器的存放key
//        String sessionId = headerAccessor.getSessionAttributes().get(Constants.SESSIONID) + "";
        logger.info("stomp Subscribe : " + headerAccessor.getMessageHeaders());
    }
}