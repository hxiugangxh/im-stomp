package com.us.example.service;

import com.us.example.bean.ImUser;
import com.us.example.bean.OnlineInfoBean;

import java.util.List;

public interface ImService {
    String getNick(String userName);

    OnlineInfoBean listOnlineUser(List<String> onlineUserList);
}
