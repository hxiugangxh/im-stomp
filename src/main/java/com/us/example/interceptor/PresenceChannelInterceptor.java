package com.us.example.interceptor;

import java.util.LinkedList;
import java.util.Map;

import com.us.example.bean.MyPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {

    /**
     * 获取包含在stomp中的用户信息
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {
                Object name = ((Map) raw).get("name");
                if (name instanceof LinkedList) {
                    // 设置当前访问器的认证用户
                    accessor.setUser(new MyPrincipal(((LinkedList) name).get(0).toString()));
                }
            }
        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
        // ignore non-STOMP messages like heartbeat messages
        if (sha.getCommand() == null) {
            return;
        }
        //这里的sessionId和accountId对应HttpSessionIdHandshakeInterceptor拦截器的存放key
        /*String sessionId = sha.getSessionAttributes().get(Constants.SESSIONID).toString();
        String accountId = sha.getSessionAttributes().get(Constants.SKEY_ACCOUNT_ID).toString();*/
        String sessionId = "";
        String accountId = "";
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

    private void connect(String sessionId, String accountId) {
        log.info(" STOMP Connect [sessionId: " + sessionId + ", accountId: " + accountId + "]");
        //存放至ehcache
        /*String cacheName = CacheConstant.WEBSOCKET_ACCOUNT;
        //若在多个浏览器登录，直接覆盖保存
        EhcacheUtil.getInstance().put(cacheName, cacheName + accountId, sessionId);*/
    }

    private void disconnect(String sessionId, String accountId) {
        log.info(" STOMP Connect [sessionId: " + sessionId + ", accountId: " + accountId + "]");
    }

}