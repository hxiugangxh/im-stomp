package com.ylz.imstomp.service.impl;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.dao.jpa.ImChatLogDao;
import com.ylz.imstomp.service.ImChatLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("imChatLogService")
public class ImChatLogServieImpl implements ImChatLogService {

    @Autowired
    private ImChatLogDao imChatLogDao;

    @Override
    public void saveChatLog(ChatMessage chatMessage) {
        imChatLogDao.save(chatMessage);
    }
}
