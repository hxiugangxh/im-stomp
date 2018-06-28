package com.ylz.imstomp.mq;

import com.ylz.imstomp.bean.OnlineInfoBean;
import com.ylz.imstomp.constant.AMQConstants;
import com.ylz.imstomp.constant.Constants;
import com.ylz.imstomp.service.ImService;
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

    @RabbitListener(queues = "stompDisconnect")
    public void brokerDisconnect(String accountId) {
        log.info("通知下线 = " + accountId);
        List<String> onlineUserList = new ArrayList<>();
        for (SimpUser simpUser : simpUserRegistry.getUsers()) {
            onlineUserList.add(simpUser.getName());
        }

        OnlineInfoBean onlineInfoBean = imService.listOnlineUser(onlineUserList);

        simpMessagingTemplate.convertAndSend(Constants.USER_ONLINE_INFO, onlineInfoBean);
    }

}
