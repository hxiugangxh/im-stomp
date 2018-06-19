package com.us.example.bean;

import lombok.Data;

@Data
public class ImUser {
    private Integer id;
    private String userName;
    private String nick;
    private String online = "0";
}
