package com.ylz.imstomp.service.impl;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.dao.mongodb.ImChatLogMongo;
import com.ylz.imstomp.service.ImChatLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("imChatLogService")
public class ImChatLogServieImpl implements ImChatLogService {

    @Autowired
    private ImChatLogMongo imChatLogMongo;

    @Override
    public void saveChatLog(ChatMessage chatMessage) {
        imChatLogMongo.insert(chatMessage);
    }
}
