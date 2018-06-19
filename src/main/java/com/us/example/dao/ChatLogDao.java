package com.us.example.dao;

import com.us.example.bean.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatLogDao extends JpaRepository<ChatMessage, Integer> {
}
