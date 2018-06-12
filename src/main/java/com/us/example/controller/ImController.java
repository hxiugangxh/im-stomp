package com.us.example.controller;

import com.us.example.bean.ChatUserBean;
import com.us.example.bean.GroupChatMessage;
import com.us.example.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

@Controller
public class ImController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @RequestMapping("/chatRoom")
    public String chatRoom(Map<String, Object> map, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        ChatUserBean chatUserBean = new ChatUserBean();
        chatUserBean.setUserName(userDetails.getUsername());

        String nick = "";
        if (userDetails.getUsername().equals("admin")) {
            nick = "管理员";
        } else {
            nick = "路人" + userDetails.getUsername();
        }
        chatUserBean.setNick(nick);
        // 改成成登录信息
        map.put("chatUserBean", chatUserBean);

        return "chat_room";
    }

    @RequestMapping("/groupChat")
    @ResponseBody
    public String groupChat(GroupChatMessage groupChatMessage, Authentication authentication) {

        String userName = ((UserDetails) authentication.getPrincipal()).getUsername();
        groupChatMessage.setUserName(userName);
        groupChatMessage.setSendTime(new Date());

        simpMessagingTemplate.convertAndSend(Constants.GROUP_CHAT_DES, groupChatMessage);

        return "test";
    }

}
