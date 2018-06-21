package com.ylz.imstomp.dao.mongodb;

import com.ylz.imstomp.bean.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImChatLogMongo extends MongoRepository<ChatMessage, Integer> {

    Page<ChatMessage> findChatMessagesByType(Integer type, Pageable pageable);

}
