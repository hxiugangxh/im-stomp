package com.ylz.imstomp.service;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.ImUser;
import com.ylz.imstomp.bean.OnlineInfoBean;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ImService {
    String getNick(String userName);

    OnlineInfoBean listOnlineUser(List<String> onlineUserList, String userName);

    Page<ChatMessage> listChatMessage(Integer type, String userName, String toUserName, Integer pn, Integer pageSize);

    void readChatMessage(String fromUserName, String toUserName);

    ImUser getImUserCount(String fromUserName, String toUserName);
}
