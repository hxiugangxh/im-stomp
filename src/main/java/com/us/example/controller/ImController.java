package com.us.example.controller;

import com.us.example.bean.ChatUserBean;
import com.us.example.bean.GroupChatMessage;
import com.us.example.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

@Controller
public class ImController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping("/look")
    @ResponseBody
    public String look() {

        Cache cache = cacheManager.getCache("websocket_account");

        System.out.println("admin = " + cache.get("websocket_accountadmin", String.class));
        System.out.println("abel = " + cache.get("websocket_accountabel", String.class));

        return "test";
    }

    @RequestMapping("/chatRoom")
    public String chatRoom(Map<String, Object> map, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String userName = userDetails.getUsername();
        ChatUserBean chatUserBean = new ChatUserBean();
        chatUserBean.setUserName(userName);

        String nick = "";
        if (userDetails.getUsername().equals("admin")) {
            nick = "管理员";
        } else {
            nick = "路人" + userName;
        }
        chatUserBean.setNick(nick);

        map.put("chatUserBean", chatUserBean);

        return "chat_room";
    }

    @RequestMapping("/brokerOnline")
    @ResponseBody
    public String brokerOnline() {
        GroupChatMessage groupChatMessage = new GroupChatMessage();
        groupChatMessage.setUserCount(simpUserRegistry.getUserCount());

        simpMessagingTemplate.convertAndSend("/topic/online", groupChatMessage);

        return "test";
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
