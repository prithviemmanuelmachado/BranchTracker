package com.picnote.gateway.gateway_broker.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Controller;

@Controller
public class BranchController {

    private Logger logger;
    private SessionRepository<MapSession> sessionRepository;

    public BranchController(SessionRepository<MapSession> sessionRepository){
        this.logger = LoggerFactory.getLogger(BranchController.class);
        this.sessionRepository = sessionRepository;
    }

    @MessageMapping("/test")
    @SendTo("/topic/test")
    public Object test(@Payload Map<String, String> message){
        try {
            logger.info("payload " + message);
            Map<String, String> res = new HashMap<>();
            res.put("body", "done " + message.get("name"));
            return res;  
        } catch (Exception e) {
            logger.error(message.toString(), e);
            return "err " + message;
        }
    }

}
