package com.ylz.imstomp.dao.mongodb;

import com.ylz.imstomp.bean.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImChatLogMongo extends MongoRepository<ChatMessage, Integer> {

    Page<ChatMessage> findChatMessagesByType(Integer type, Pageable pageable);

    Page<ChatMessage> findChatMessagesByTypeAndFromUserNameOrToUserName(
            Integer type, String fromUserName, String toUserName, Pageable pageable);
}
