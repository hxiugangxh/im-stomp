package com.ylz.imstomp.bean;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@Document(collection = "cm_chat_message")
public class ChatMessage implements Serializable {
    @Id
    private String id;
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