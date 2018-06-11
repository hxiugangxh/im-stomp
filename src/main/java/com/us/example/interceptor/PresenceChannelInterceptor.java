package com.us.example.interceptor;

import com.us.example.constant.CacheConstant;
import com.us.example.constant.Constants;
import com.us.example.util.EhCacheUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
        // ignore non-STOMP messages like heartbeat messages
        if (sha.getCommand() == null) {
            return;
        }

        //这里的sessionId和accountId对应HttpSessionIdHandshakeInterceptor拦截器的存放key
        String sessionId = sha.getSessionAttributes().get(Constants.SESSIONID) + "";
        String accountId = sha.getSessionAttributes().get(Constants.SKEY_ACCOUNT_ID) + "";
        //判断客户端的连接状态
        switch (sha.getCommand()) {
            case CONNECT:
                connect(sessionId, accountId);
                break;
            case CONNECTED:
                break;
            case DISCONNECT:
                disconnect(sessionId, accountId);
                break;
            default:
                break;
        }
    }

    @Autowired
    private CacheManager cacheManager;

    private void connect(String sessionId, String accountId) {
        log.info(" STOMP Connect [sessionId: " + sessionId + ", accountId: " + accountId + "]");
        //存放至ehcache
        String cacheName = CacheConstant.WEBSOCKET_ACCOUNT;
        //若在多个浏览器登录，直接覆盖保存
        Cache cache = cacheManager.getCache("websocket_account");

        Element e = new Element(cacheName + accountId, sessionId);
        cache.put(cacheName + accountId, sessionId);

        System.out.println("key = " + cacheName + accountId);
        System.out.println("value = " + sessionId);
        System.out.println(cache.get(cacheName + accountId));

        System.out.println(cache);
    }

    private void disconnect(String sessionId, String accountId) {
        log.info(" STOMP Connect [sessionId: " + sessionId + ", accountId: " + accountId + "]");
    }

}