package com.example.branch_tracker.branch_tracker.consumer;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.branch_tracker.branch_tracker.services.AuthenticationService;
import com.example.branch_tracker.branch_tracker.services.BranchServices;

@Service
public class BranchConsumer {

    private RabbitTemplate rabbitTemplate;
    private BranchServices branchServices;
    private Logger logger;
    private AuthenticationService auth;

    public BranchConsumer(
        RabbitTemplate rabbitTemplate, 
        BranchServices branchServices, 
        AuthenticationService auth
    ){
        this.rabbitTemplate = rabbitTemplate;
        this.branchServices = branchServices;
        this.logger = LoggerFactory.getLogger(BranchConsumer.class);
        this.auth = auth;
    }

    public void sendResponse(String senderId, String correlationId, Object data) {
        this.rabbitTemplate.convertAndSend(senderId, data, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setCorrelationId(correlationId);
            return message;
        });
    }

    @RabbitListener(queues = {"${rabbitmq.queue.branch.name}"})
    public void consumeMessage(
        @Header(value = AmqpHeaders.REPLY_TO, required = false) String senderId,
        @Header(value = AmqpHeaders.CORRELATION_ID, required = false) String correlationId,
        @Header(required = true) String method,
        Map<String, Object> payload
    ){
        try {
            if(payload.get("token") != null){
                Map<String, String> decodedToken = auth.validateUser(payload.get("token").toString());
                if(decodedToken.containsKey("error")){
                    if (senderId != null && correlationId != null){
                        Map<String, String> response = new HashMap<>();
                        response.put("message", decodedToken.get("error"));
                        response.put("code", "401");
                        this.sendResponse(senderId, correlationId, response);
                    }
                }else{
                    if (senderId != null && correlationId != null) {
                        // for request response type messages 
                        Map<String, Object> response = new HashMap<>();
                        if(method.toLowerCase().equals("validate_branch".toLowerCase())){
                            response = branchServices.validateBranch(payload, decodedToken);
                        }else if(method.toLowerCase().equals("create_branch".toLowerCase())){
                            response = branchServices.addBranch(payload, decodedToken);
                        } else if(method.toLowerCase().equals("get_all_branches".toLowerCase())){
                            response = branchServices.getAllBranches(payload, decodedToken);
                        } else if(method.toLowerCase().equals("get_branch_details".toLowerCase())){
                            response = branchServices.getBrancheDetails(payload, decodedToken);
                        } else if(method.toLowerCase().equals("add_task".toLowerCase())){
                            response = branchServices.addTask(payload, decodedToken);
                        } else if(method.toLowerCase().equals("get_users_for_branch".toLowerCase())){
                            response = branchServices.getUsersForBranch(payload, decodedToken);
                        } else if(method.toLowerCase().equals("add_message".toLowerCase())){
                            response = branchServices.addMessage(payload, decodedToken);
                        }
                        this.sendResponse(senderId, correlationId, response);
                    } else {
                        //for request only messages
                        if(method.toLowerCase().equals("peer_action".toLowerCase())){
                            branchServices.peerAction(payload, decodedToken);
                        } else if(method.toLowerCase().equals("move_branch".toLowerCase())){
                            branchServices.moveBranch(payload, decodedToken);
                        } else if(method.toLowerCase().equals("remove_task".toLowerCase())){
                            branchServices.removeTask(payload, decodedToken);
                        }
                    }   
                } 
            }else{
                if (senderId != null && correlationId != null){
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Invalid token. Please login");
                    response.put("code", "401");
                    this.sendResponse(senderId, correlationId, response);
                }
            }       
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("error in " + method, e);
            if (senderId != null && correlationId != null){
                Map<String, String> response = new HashMap<>();
                response.put("message", "An error occured in the server. Please try agian later");
                response.put("code", "500");
                this.sendResponse(senderId, correlationId, response);
            }
        }
    }

}
