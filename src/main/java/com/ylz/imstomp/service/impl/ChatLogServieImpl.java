package com.ylz.imstomp.service.impl;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.dao.jpa.ChatLogDao;
import com.ylz.imstomp.service.ChatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("chatLogService")
public class ChatLogServieImpl implements ChatLogService {

    @Autowired
    private ChatLogDao chatLogDao;

    @Override
    public void save(ChatMessage chatMessage) {
        chatLogDao.save(chatMessage);
    }
}
