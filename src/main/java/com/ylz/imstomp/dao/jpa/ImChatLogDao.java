package com.ylz.imstomp.dao.jpa;

import com.ylz.imstomp.bean.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImChatLogDao extends JpaRepository<ChatMessage, Integer> {
}
