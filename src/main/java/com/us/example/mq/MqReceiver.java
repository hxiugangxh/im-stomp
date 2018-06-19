package com.us.example.mq;

import com.us.example.bean.OnlineInfoBean;
import com.us.example.constant.AMQConstants;
import com.us.example.constant.Constants;
import com.us.example.service.ImService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MqReceiver {

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ImService imService;

    @RabbitListener(queuesToDeclare = @Queue("myQueue"))
    public void process(String message) {
        log.info("MqReceiver.process: {}", message);
    }

    @RabbitListener(queuesToDeclare = @Queue(AMQConstants.BROKER_STOMP_DISCONNECT))
    public void brokerDisconnect(String accountId) {

        log.info(accountId + "离线");
        List<String> onlineUserList = new ArrayList<>();
        for (SimpUser simpUser : simpUserRegistry.getUsers()) {
            onlineUserList.add(simpUser.getName());
        }

        OnlineInfoBean onlineInfoBean = imService.listOnlineUser(onlineUserList);

        simpMessagingTemplate.convertAndSend(Constants.USER_ONLINE_INFO, onlineInfoBean);
    }

}
