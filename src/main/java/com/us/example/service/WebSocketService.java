package com.us.example.service;

import com.us.example.bean.GroupChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    //使用SimpMessagingTemplate 向浏览器发送消息
    private SimpMessagingTemplate template;

    public void sendMessage() throws Exception {
        for (int i = 0; i < 10; i++) {
            template.convertAndSend("/topic/response", new GroupChatMessage("Welcome,yangyibo !" + i));
            System.out.println("----------------------yangyibo" + i);
        }
    }

}
