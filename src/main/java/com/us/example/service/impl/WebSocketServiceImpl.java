package com.us.example.service.impl;

import com.us.example.bean.ChatMessage;
import com.us.example.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    //使用SimpMessagingTemplate 向浏览器发送消息
    private SimpMessagingTemplate template;

    public void sendMessage() throws Exception {
        for (int i = 0; i < 10; i++) {
            template.convertAndSend("/topic/response", new ChatMessage("Welcome,yangyibo !" + i));
            System.out.println("----------------------yangyibo" + i);
        }
    }

}
