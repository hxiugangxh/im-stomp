package com.ylz.imstomp.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OnlineInfoBean {
    private List<ImUser> imUserList;
    private Integer userCount;
    private Map<String, Map<String, Integer>> map;
}
