package com.us.example.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

@Component
public class TestService {


    public void test() {
        System.out.println("simpMessagingTemplate = ");
        System.out.println("simpUserRegistry = ");
    }

}
