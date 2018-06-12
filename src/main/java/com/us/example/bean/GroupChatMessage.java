package com.us.example.bean;

import lombok.Data;

import java.util.Date;

@Data
public class GroupChatMessage {
    private String user; // 用户
    private String nick; // 名称
    private String online; // 0：离线；1：在线
    private String msg; // 消息
    private Date sendTime; // 发送时间
    private Date reviceTime; // 接收时间

    public GroupChatMessage() {}

    public GroupChatMessage(String msg) {
        this.msg = msg;
    }
}