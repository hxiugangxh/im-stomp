package com.us.example.service.impl;

import com.us.example.bean.ImUser;
import com.us.example.bean.OnlineInfoBean;
import com.us.example.dao.ImDao;
import com.us.example.service.ImService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("imService")
public class ImServiceImpl implements ImService {

    @Autowired
    private ImDao imDao;

    @Override
    public String getNick(String userName) {
        return imDao.getNick(userName);
    }

    @Override
    public OnlineInfoBean listOnlineUser(List<String> onlineUserList) {
        return imDao.listOnlineUser(onlineUserList);
    }
}
