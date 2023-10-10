package com.picnote.users.consumers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.picnote.gateway.gateway_broker.models.User;
import com.picnote.users.models.UserCollection;
import com.picnote.users.services.UserService;

@Service
public class UserConsumer {

    private RabbitTemplate rabbitTemplate;
    private UserService userService;
    private Logger logger;

    public UserConsumer(RabbitTemplate rabbitTemplate, UserService userService){
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
        this.logger = LoggerFactory.getLogger(UserConsumer.class);
    }

    public void sendResponse(String senderId, String correlationId, Object data) {
        this.rabbitTemplate.convertAndSend(senderId, data, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setCorrelationId(correlationId);
            return message;
        });
    }

    @RabbitListener(queues = {"${rabbitmq.queue.user.name}"})
    public void consumeMessage(
        @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
        @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId,
        @Header(required = true) String method,
        User user
    )
    {
        try {
            UserCollection u = new UserCollection();
            u.setUserID(user.getUserID().toString());
            u.setEmail(user.getEmail());
            u.setName(user.getName());
            u.setPassword(user.getPassword());
            if (senderId != null && correlationId != null) {
                // for request response type messages 
                Map<String, String> response = new HashMap<>();
                if(method.toLowerCase().equals("register".toLowerCase())){
                    response = userService.addUser(u);
                }
                this.sendResponse(senderId, correlationId, response);
            } else {
                //for request only messages
            }            
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("error in " + method, e);
            if (senderId != null && correlationId != null){
                Map<String, String> response = new HashMap<>();
                response.put("message", "An error occured in the server. Please try agian later");
                response.put("status", "error");
                this.sendResponse(senderId, correlationId, response);
            }
        }
    }
    
}
