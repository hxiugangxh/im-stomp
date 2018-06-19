package com.us.example.service;

import org.springframework.stereotype.Service;

@Service
public interface WebSocketService {

    void sendMessage() throws Exception;

}
