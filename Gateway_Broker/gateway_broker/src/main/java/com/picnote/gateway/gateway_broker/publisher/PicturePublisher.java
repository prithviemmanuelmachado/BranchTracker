package com.picnote.gateway.gateway_broker.publisher;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PicturePublisher {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.picture.key}")
    private String pictureQueueKey;

    private RabbitTemplate rabbitTemplate;

    public PicturePublisher(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Object body, String method){
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().getHeaders().put("method", method);
            return message;
        };
        rabbitTemplate.convertAndSend(exchangeName, pictureQueueKey, body, messagePostProcessor);
    }

    public Object sendAndReciveMessage(Object body, String method){
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().getHeaders().put("method", method);
            return message;
        };
        return rabbitTemplate.convertSendAndReceive(exchangeName, pictureQueueKey, body, messagePostProcessor);
    }
}
