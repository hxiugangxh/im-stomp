package com.us.example.dao;

import com.us.example.bean.ImUser;
import com.us.example.bean.OnlineInfoBean;

import java.util.List;

public interface ImDao {
    String getNick(String userName);

    OnlineInfoBean listOnlineUser(List<String> onlineUserList);
}
