package com.ylz.imstomp.dao.jpa;

import com.ylz.imstomp.bean.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("imChatLogDao")
public interface ImChatLogDao extends JpaRepository<ChatMessage, Integer> {
}
