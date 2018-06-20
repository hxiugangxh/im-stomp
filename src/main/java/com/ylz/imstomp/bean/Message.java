package com.ylz.imstomp.bean;

import lombok.Data;

/**
 * 浏览器向服务器发送的消息使用此类接受
 */
@Data
public class Message {
    private Integer type;   //聊天类型，0：群聊；1：单聊;
    private String fromUser;//发送者.
    private String toUser;//接受者
    private String msg;//消息.
}