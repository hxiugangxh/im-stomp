package com.ylz.imstomp.bean;

import lombok.Data;

import java.util.List;

@Data
public class OnlineInfoBean {
    private List<ImUser> imUserList;
    private Integer userCount;
}
