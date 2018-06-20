package com.ylz.imstomp.service;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.OnlineInfoBean;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ImService {
    String getNick(String userName);

    OnlineInfoBean listOnlineUser(List<String> onlineUserList);

    Page<ChatMessage> listChatMessage(Integer type, String userName);
}
