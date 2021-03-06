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

    private Integer readFlag = 0; // 0：未读；1：已读

    private Integer noReadCount; // 未读总数

    // filepath + md5(目录) + name(文件)
    private String md5;
    private String name;

    public ChatMessage() {}

    public ChatMessage(String msg) {
        this.msg = msg;
    }
}