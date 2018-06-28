package com.ylz.imstomp.service.impl;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.dao.mongodb.ImChatLogMongoJpa;
import com.ylz.imstomp.service.ImChatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("imChatLogService")
public class ImChatLogServieImpl implements ImChatLogService {

    @Autowired
    private ImChatLogMongoJpa imChatLogMongo;

    @Override
    public void saveChatLog(ChatMessage chatMessage) {
        imChatLogMongo.insert(chatMessage);
    }
}
