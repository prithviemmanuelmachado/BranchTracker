package com.picnote.gateway.gateway_broker.configuration;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.user.name}")
    private String userQueueName;
    
    @Value("${rabbitmq.queue.user.key}")
    private String userQueueKey;

    @Value("${rabbitmq.queue.picture.name}")
    private String pictureQueueName;
    
    @Value("${rabbitmq.queue.picture.key}")
    private String pictureQueueKey;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    //spring bean for rabbitmq queue - user
    @Bean
    public Queue userQueue(){
        return new Queue(userQueueName);
    }

    //spring bean for rabbitmq queue - picture
    @Bean
    public Queue pictureQueue(){
        return new Queue(pictureQueueName);
    }

    //spring bean for rabbitmq exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchangeName);
    }

    //binding queue to exchnage - user
    @Bean
    public Binding bindingUser(){
        return BindingBuilder
                .bind(userQueue())
                .to(exchange())
                .with(userQueueKey);
    }

    //binding queue to exchnage - picture
    @Bean
    public Binding bindingPicture(){
        return BindingBuilder
                .bind(pictureQueue())
                .to(exchange())
                .with(pictureQueueKey);
    }

    //initializing obj to json convertor for message body
    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
    
}
