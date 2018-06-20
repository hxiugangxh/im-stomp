package com.ylz.imstomp.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class MqController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping("/sendMQ")
    public String sendMQ() {

        amqpTemplate.convertAndSend("myQueue", new Date());

        return "test";
    }

}
