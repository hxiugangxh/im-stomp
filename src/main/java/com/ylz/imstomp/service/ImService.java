package com.ylz.imstomp.service;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.OnlineInfoBean;

import java.util.List;

public interface ImService {
    String getNick(String userName);

    OnlineInfoBean listOnlineUser(List<String> onlineUserList);

    List<ChatMessage> listChatMessage(Integer type, String userName);
}
