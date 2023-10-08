package com.picnote.users.consumers;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.picnote.gateway.gateway_broker.models.User;

@Service
public class UserConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
        User message
    )
    {
        if (senderId != null && correlationId != null) {
            String responseMessage = "Test successful for " + message.getEmail() + " at " + method;
            this.sendResponse(senderId, correlationId, responseMessage);
        }
    }
    
}
