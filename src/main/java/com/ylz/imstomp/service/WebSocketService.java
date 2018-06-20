package com.ylz.imstomp.service;

import org.springframework.stereotype.Service;

@Service
public interface WebSocketService {

    void sendMessage() throws Exception;

}
