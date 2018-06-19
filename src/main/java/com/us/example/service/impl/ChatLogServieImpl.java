package com.us.example.service.impl;

import com.us.example.bean.ChatMessage;
import com.us.example.dao.ChatLogDao;
import com.us.example.service.ChatLogService;
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
