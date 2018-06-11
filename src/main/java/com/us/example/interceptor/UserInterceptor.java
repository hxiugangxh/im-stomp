package com.us.example.interceptor;

import java.util.LinkedList;
import java.util.Map;

import com.us.example.bean.User;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;


/**
 * @author cheng
 * @ClassName: UserInterceptor
 * @Description: 客户端渠道拦截适配器
 * @date 2017年9月29日 下午2:40:12
 */
public class UserInterceptor extends ChannelInterceptorAdapter {

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
                    accessor.setUser(new User(((LinkedList) name).get(0).toString()));
                }
            }
        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
        // ignore non-STOMP messages like heartbeat messages
        if(sha.getCommand() == null) {
            return;
        }
        //这里的sessionId和accountId对应HttpSessionIdHandshakeInterceptor拦截器的存放key
        //判断客户端的连接状态
        switch(sha.getCommand()) {
            case CONNECT:
                connect("","");
                break;
            case CONNECTED:
                connnected();
                break;
            case DISCONNECT:
                disconnect();
                break;
            default:
                break;
        }
    }

    private void connect(String sessionId,String accountId){
        System.out.println(" STOMP Connect [sessionId: " + sessionId + "]");
       /* //存放至ehcache
        String cacheName = CacheConstant.WEBSOCKET_ACCOUNT;
        //若在多个浏览器登录，直接覆盖保存
        CacheManager.put(cacheName ,cacheName+accountId,sessionId);*/
    }

    private void disconnect(){
        System.out.println(" STOMP disconnect");
    }

    private void connnected(){
        System.out.println(" STOMP disconnect");
    }
}