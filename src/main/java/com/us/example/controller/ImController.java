package com.us.example.controller;

import com.us.example.bean.ChatMessage;
import com.us.example.bean.ImUser;
import com.us.example.bean.OnlineInfoBean;
import com.us.example.constant.Constants;
import com.us.example.service.ImService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class ImController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ImService imService;

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
        ImUser imUser = new ImUser();
        imUser.setUserName(userName);

        imUser.setNick(imService.getNick(userName));

        map.put("imUser", imUser);

        return "chat_room";
    }

    @RequestMapping("/brokerOnline")
    @ResponseBody
    public String brokerOnline() {
        List<String> onlineUserList = new ArrayList<>();
        for (SimpUser simpUser : simpUserRegistry.getUsers()) {
            onlineUserList.add(simpUser.getName());
        }

        OnlineInfoBean onlineInfoBean = imService.listOnlineUser(onlineUserList);

        simpMessagingTemplate.convertAndSend(Constants.USER_ONLINE_INFO, onlineInfoBean);

        return "test";
    }

    @RequestMapping("/groupChat")
    @ResponseBody
    public String groupChat(ChatMessage chatMessage, Authentication authentication) {

        String userName = ((UserDetails) authentication.getPrincipal()).getUsername();
        chatMessage.setUserName(userName);
        chatMessage.setSendTime(new Date());

        simpMessagingTemplate.convertAndSend(Constants.GROUP_CHAT_DES, chatMessage);

        return "test";
    }



}
