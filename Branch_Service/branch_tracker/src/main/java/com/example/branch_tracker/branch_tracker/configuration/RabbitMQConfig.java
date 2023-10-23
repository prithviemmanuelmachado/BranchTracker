package com.example.branch_tracker.branch_tracker.configuration;

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

    @Value("${rabbitmq.queue.branch.name}")
    private String branchQueueName;
    
    @Value("${rabbitmq.queue.branch.key}")
    private String branchQueueKey;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    //spring bean for rabbitmq queue - branch
    @Bean
    public Queue branchQueue(){
        return new Queue(branchQueueName);
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
                .bind(branchQueue())
                .to(exchange())
                .with(branchQueueKey);
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
