package com.ylz.imstomp.service.impl;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.ImUser;
import com.ylz.imstomp.bean.OnlineInfoBean;
import com.ylz.imstomp.dao.mapper.ImUserMapper;
import com.ylz.imstomp.service.ImService;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service("imService")
public class ImServiceImpl implements ImService {

    @Autowired
    private ImUserMapper imUserMapper;

    @Override
    public String getNick(String userName) {
        List<Map<String, Object>> list = imUserMapper.getNick(userName);

        return (list.size() > 0) ? MapUtils.getString(list.get(0), "nick", "") : "";
    }

    @Override
    public OnlineInfoBean listOnlineUser(List<String> onlineUserList) {

        List<ImUser> list = imUserMapper.listOnlineUser(onlineUserList);

        AtomicReference<Integer> userCount = new AtomicReference<>(0);
        List<ImUser> imUserList = list.stream().map(imUser -> {
            if (onlineUserList.contains(imUser.getUserName())) {
                imUser.setOnline("1");
                userCount.getAndSet(userCount.get() + 1);
            }
            return imUser;
        }).collect(Collectors.toList());

        OnlineInfoBean onlineInfoBean = new OnlineInfoBean();
        onlineInfoBean.setImUserList(imUserList);
        onlineInfoBean.setUserCount(userCount.get());
        return onlineInfoBean;
    }

    @Override
    public List<ChatMessage> listChatMessage(String userName, String type) {
        return imUserMapper.listChatMessage(userName, type);
    }
}
