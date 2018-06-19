package com.us.example.controller;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.us.example.bean.ChatMessage;
import com.us.example.bean.ImUser;
import com.us.example.bean.OnlineInfoBean;
import com.us.example.constant.Constants;
import com.us.example.service.ChatLogService;
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

import java.util.*;

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

    @Autowired
    private ChatLogService chatLogService;

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
    public Map<String, Object> brokerOnline() {
        Map<String, Object> jsonMap = new HashMap<>();
        boolean flag = true;
        try {
            List<String> onlineUserList = new ArrayList<>();
            for (SimpUser simpUser : simpUserRegistry.getUsers()) {
                onlineUserList.add(simpUser.getName());
            }

            OnlineInfoBean onlineInfoBean = imService.listOnlineUser(onlineUserList);

            simpMessagingTemplate.convertAndSend(Constants.USER_ONLINE_INFO, onlineInfoBean);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        jsonMap.put("flag", flag);

        return jsonMap;
    }

    @RequestMapping("/groupChat")
    @ResponseBody
    public Map<String, Object> groupChat(ChatMessage chatMessage) {
        Map<String, Object> jsonMap = new HashMap<>();
        boolean flag = true;
        try {
            chatLogService.save(chatMessage);
            simpMessagingTemplate.convertAndSend(Constants.GROUP_CHAT_DES, chatMessage);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        jsonMap.put("flag", flag);

        return jsonMap;
    }

}
