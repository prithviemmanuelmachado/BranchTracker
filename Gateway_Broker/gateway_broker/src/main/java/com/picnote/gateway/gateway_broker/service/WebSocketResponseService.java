package com.picnote.gateway.gateway_broker.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WebSocketResponseService {

    @Autowired
    private SimpMessagingTemplate smt;

    @Async
    public void sendNotification(Map<String, Object> message, List<String> ids, String method){
        message.put("method", method);
        ids.forEach((id) -> {
            smt.convertAndSendToUser(id, "/branch", message);
        });
    }

    public void sendResponse(Map<String, Object> message, SimpMessageHeaderAccessor header, String method){
        String id = (String) header.getSessionAttributes().get("username");
        message.put("method", method);
        smt.convertAndSendToUser(id, "/branch", message);
    }

}
