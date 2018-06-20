package com.ylz.imstomp.dao.mapper;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.ImUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ImUserMapper {
    List<Map<String, Object>> getNick(@Param("userName") String userName);

    List<ImUser> listOnlineUser(@Param("onlineUserList") List<String> onlineUserList);

    List<ChatMessage> listChatMessage(@Param("userName") String userName, @Param("type") String type);
}
