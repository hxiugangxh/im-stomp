package com.ylz.imstomp.service.impl;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.ImUser;
import com.ylz.imstomp.bean.OnlineInfoBean;
import com.ylz.imstomp.dao.mapper.ImUserMapper;
import com.ylz.imstomp.dao.mongodb.ImChatMongoDao;
import com.ylz.imstomp.service.ImService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service("imService")
public class ImServiceImpl implements ImService {

    @Autowired
    private ImUserMapper imUserMapper;

    @Autowired
    private ImChatMongoDao imChatMongoDao;

    @Override
    public String getNick(String userName) {
        List<Map<String, Object>> list = imUserMapper.getNick(userName);

        return (list.size() > 0) ? MapUtils.getString(list.get(0), "nick", "") : "";
    }

    @Override
    public OnlineInfoBean listOnlineUser(List<String> onlineUserList, String userName) {

        List<ImUser> list = imUserMapper.listOnlineUser(onlineUserList);

        // 汇聚未读数据
        List<ChatMessage> chatMessageList = imChatMongoDao.listNoReadChatMessage();

        AtomicReference<Integer> userCount = new AtomicReference<>(0);
        List<ImUser> imUserList = list.stream().map(imUser -> {
            if (onlineUserList.contains(imUser.getUserName())) {
                imUser.setOnline("1");
                userCount.getAndSet(userCount.get() + 1);
            }
            chatMessageList.forEach(chatMessage -> {
                if (userName.equals(chatMessage.getToUserName())
                        && imUser.getUserName().equals(chatMessage.getFromUserName())) {
                    imUser.setNoReadCount(chatMessage.getNoReadCount());
                }
            });
            return imUser;
        }).collect(Collectors.toList());

        OnlineInfoBean onlineInfoBean = new OnlineInfoBean();
        onlineInfoBean.setImUserList(imUserList);
        onlineInfoBean.setUserCount(userCount.get());

        return onlineInfoBean;
    }

    @Override
    public List<ChatMessage> listChatMessage(Integer type, String fromUserName, String toUserName, Integer pn,
                                             Integer pageSize) {

        return imChatMongoDao.listChatMessage(type, fromUserName, toUserName, pn, pageSize);
    }

    @Override
    public void readChatMessage(String fromUserName, String toUserName) {
        imChatMongoDao.readChatMessage(fromUserName, toUserName);
    }

    @Override
    public ImUser getImUserCount(String fromUserName, String toUserName) {
        return imChatMongoDao.getImUserCount(fromUserName, toUserName);
    }
}
