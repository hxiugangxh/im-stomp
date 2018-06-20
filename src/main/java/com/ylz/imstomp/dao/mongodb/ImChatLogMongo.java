package com.ylz.imstomp.dao.mongodb;

import com.ylz.imstomp.bean.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImChatLogMongo extends MongoRepository<ChatMessage, Integer> {

    List<ChatMessage> findChatMessagesByType(Integer type);

    List<ChatMessage> findChatMessagesByTypeAndFromUserNameOrToUserName(
            Integer type, String fromUserName, String toUserName);
}
