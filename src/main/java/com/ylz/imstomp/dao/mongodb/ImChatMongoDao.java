package com.ylz.imstomp.dao.mongodb;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.ImUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface ImChatMongoDao {
    Page<ChatMessage> listChatMessage(Integer type, String fromUserName, String toUserName, Integer pn, Integer pageSize);

    List<ChatMessage> listNoReadChatMessage();

    void readChatMessage(String fromUserName, String toUserName);

    ImUser getImUserCount(String fromUserName, String toUserName);
}
