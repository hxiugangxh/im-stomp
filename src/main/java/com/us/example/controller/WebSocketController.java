package com.us.example.controller;

import com.us.example.bean.GroupChatMessage;
import com.us.example.bean.Message;
import com.us.example.bean.OnLineBean;
import com.us.example.constant.CacheConstant;
import com.us.example.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Controller
public class WebSocketController {
    @Autowired
    private WebSocketService ws;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {

        //存放至ehcache
        String cacheName = CacheConstant.WEBSOCKET_ACCOUNT;

        Cache cache = cacheManager.getCache(cacheName);

        String sessionId = cache.get("websocket_accountadmin", String.class);

        System.out.println(sessionId);
        sessionId = cache.get("websocket_accountabel", String.class);
        System.out.println(sessionId);

        return "test";
    }

    @RequestMapping("/listOnline")
    @ResponseBody
    public List<OnLineBean> listOnline() {

        List<OnLineBean> onLineBeanList = new ArrayList<>();
        for (SimpUser user : simpUserRegistry.getUsers()) {
            OnLineBean onLineBean = new OnLineBean();

            onLineBean.setName(user.getName());

            onLineBeanList.add(onLineBean);
        }

        return onLineBeanList;
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/ws")
    public String ws() {
        return "ws";
    }

    @RequestMapping(value = "/chat")
    public String chat() {
        return "chat";
    }

    //http://localhost:8080/ws
    @MessageMapping("/welcome")//浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
    @SendTo("/topic/getResponse")//服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
    public GroupChatMessage say(GroupChatMessage message) throws Exception {
        Thread.sleep(1000);
        return new GroupChatMessage("Welcome, " + message.getMsg() + "!");
    }

    @MessageMapping("/testSend")
    @SendTo("/topic/getResponse")
    public GroupChatMessage testSend() throws Exception {

        return new GroupChatMessage("/topic/getResponse测试");
    }

    //http://localhost:8080/Welcome1
    @RequestMapping("/Welcome1")
    @ResponseBody
    public String say2() throws Exception {
        ws.sendMessage();
        return "is ok";
    }

    @MessageMapping("/chat")
    //在springmvc 中可以直接获得principal,principal 中包含当前用户的信息
    public void handleChat(Principal principal, Message message) {
        System.out.println(message);

        /**
         * 此处是一段硬编码。如果发送人是wyf 则发送给 wisely 如果发送人是wisely 就发送给 wyf。
         * 通过当前用户,然后查找消息,如果查找到未读消息,则发送给当前用户。
         */
        if (principal.getName().equals("admin")) {
            //通过convertAndSendToUser 向用户发送信息,
            // 第一个参数是接收消息的用户,第二个参数是浏览器订阅的地址,第三个参数是消息本身
            messagingTemplate.convertAndSendToUser("abel",
                    "/queue/notifications", principal.getName() + "-send:"
                            + message.getMsg());
            /**
             * 72 行操作相等于 
             * messagingTemplate.convertAndSend("/user/abel/queue/notifications",principal.getName() + "-send:"
             + message.getName());
             */
        } else {
            messagingTemplate.convertAndSendToUser("admin",
                    "/queue/notifications", principal.getName() + "-send:"
                            + message.getMsg());
        }
    }
}
