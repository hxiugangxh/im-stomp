package com.ylz.imstomp.bean;

import lombok.Data;

@Data
public class ImUser {
    private Integer id;
    private String userName;
    private String nick;
    private String online = "0";
    private Integer noReadCount = 0;
}
