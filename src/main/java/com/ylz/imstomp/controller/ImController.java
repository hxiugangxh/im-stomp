package com.ylz.imstomp.controller;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.ImUser;
import com.ylz.imstomp.bean.OnlineInfoBean;
import com.ylz.imstomp.constant.Constants;
import com.ylz.imstomp.service.ImChatLogService;
import com.ylz.imstomp.service.ImService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/im")
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
    private ImChatLogService imChatLogService;

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

    @RequestMapping("/listChatMessage")
    @ResponseBody
    public List<ChatMessage> listChatMessage(
            @RequestParam("userName") String userName,
            @RequestParam("type") String type) {
        List<ChatMessage> chatMessageList = imService.listChatMessage(userName, type);

        return chatMessageList;
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
            imChatLogService.saveChatLog(chatMessage);
            simpMessagingTemplate.convertAndSend(Constants.GROUP_CHAT_DES, chatMessage);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        jsonMap.put("flag", flag);

        return jsonMap;
    }

    @RequestMapping("/chatSingleRoom")
    public String chatSingleRoom(
            @RequestParam(value = "toUserName") String toUserName,
            Map<String, Object> map,
            Principal principal) {

        ImUser imUser = new ImUser();
        imUser.setUserName(principal.getName());
        imUser.setNick(imService.getNick(principal.getName()));

        ImUser toImUser = new ImUser();
        toImUser.setUserName(toUserName);
        toImUser.setNick(imService.getNick(toUserName));

        map.put("imUser", imUser);
        map.put("toImUser", toImUser);

        return "chat_single_room";
    }

    @RequestMapping("/singleChat")
    @ResponseBody
    public Map<String, Object> singleChat(ChatMessage chatMessage, Principal principal) {
        chatMessage.setFromUserName(principal.getName());
        chatMessage.setFromNick(imService.getNick(principal.getName()));

        Map<String, Object> jsonMap = new HashMap<>();
        boolean flag = true;
        try {
            imChatLogService.saveChatLog(chatMessage);
            System.out.println(chatMessage.getToUserName());
            simpMessagingTemplate.convertAndSendToUser(chatMessage.getToUserName(),
                    Constants.SINGLE_CHAT_DES, chatMessage);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        jsonMap.put("flag", flag);

        return jsonMap;
    }

}
