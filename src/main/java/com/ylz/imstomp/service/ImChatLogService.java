package com.ylz.imstomp.service;

import com.ylz.imstomp.bean.ChatMessage;

public interface ImChatLogService {
    void saveChatLog(ChatMessage chatMessage);
}
