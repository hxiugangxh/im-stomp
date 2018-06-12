package com.us.example.controller;

import org.springframework.context.annotation.Configuration;
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

}
