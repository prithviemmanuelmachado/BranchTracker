package com.picnote.gateway.gateway_broker.publisher;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BranchPublisher {
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.branch.key}")
    private String branchQueueKey;

    private RabbitTemplate rabbitTemplate;

    public BranchPublisher(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Object body, String method){

        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().getHeaders().put("method", method);
            return message;
        };

        rabbitTemplate.convertAndSend(exchangeName, branchQueueKey, body, messagePostProcessor);
        
    }

    public Map<String, Object> sendAndReciveMessage(Object body, String method){

        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().getHeaders().put("method", method);
            return message;
        };

        Object response = rabbitTemplate.convertSendAndReceive(exchangeName, branchQueueKey, body, messagePostProcessor);
        
        Map<String, Object> obj = new HashMap<>();
        if (response instanceof Map) {
            obj = (Map<String, Object>) response;
            return obj;
        } else {
            return obj;
        }

    }
}
