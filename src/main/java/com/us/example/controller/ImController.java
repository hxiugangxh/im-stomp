package com.us.example.controller;

import com.us.example.bean.GroupChatMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ImController {

    @RequestMapping("/chatRoom")
    public String chatRoom() {

        return "chat_room";
    }

    @MessageMapping("/groupChat")//浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
    @SendTo("/topic/getResponse")//服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
    public GroupChatMessage say(GroupChatMessage message) throws Exception {
        return new GroupChatMessage("Welcome, " + message.getMsg() + "!");
    }

}
