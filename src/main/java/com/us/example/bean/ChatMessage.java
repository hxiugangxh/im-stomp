package com.us.example.bean;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "cm_chat_log")
public class ChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer type; // 0: 单聊；1；群聊
    private String fromUserName;
    private String fromNick;
    private String toUserName;
    private String toNick;
    private String msg; // 消息
    private Date sendTime; // 发送时间

    public ChatMessage() {}

    public ChatMessage(String msg) {
        this.msg = msg;
    }
}