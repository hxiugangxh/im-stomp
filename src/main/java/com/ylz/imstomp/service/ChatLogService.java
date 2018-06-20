package com.ylz.imstomp.service;

import com.ylz.imstomp.bean.ChatMessage;

public interface ChatLogService {
    void save(ChatMessage chatMessage);
}
