package com.ylz.imstomp.dao.jpa;

import com.ylz.imstomp.bean.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatLogDao extends JpaRepository<ChatMessage, Integer> {
}
