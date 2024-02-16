package com.picnote.gateway.gateway_broker.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.picnote.gateway.gateway_broker.publisher.BranchPublisher;
import com.picnote.gateway.gateway_broker.service.WebSocketResponseService;

@Controller
public class BranchController {

    private Logger logger;
    private BranchPublisher _branch;
    private WebSocketResponseService _response;

    public BranchController(
        BranchPublisher _branch,
        WebSocketResponseService _response
    ){
        this.logger = LoggerFactory.getLogger(BranchController.class);
        this._branch = _branch;
        this._response = _response;
    }

    /*
     * Request: 
     * {
     *      token: <jwt token>,
     *      branchTitle: <branch title to be verified>
     * }
     * 
     * Response:
     * {
     *      status: <exists, create>,
     *      message: <message>,
     *      code: <status code>,
     *      method: <>
     * }
    */
    @MessageMapping("/validateBranch")
    public void validateBranch(@Payload Map<String, Object> message, SimpMessageHeaderAccessor header){
        try {
            Map<String, Object> response = _branch.sendAndReciveMessage(message, "validate_branch");
            if(response.get("status").toString() == "exists" && response.get("ownerID") != null)
            {
                List<String> userIDs = Arrays.asList(response.get("ownerID").toString());

                Map<String, Object> peerMessage = new HashMap<>();
                peerMessage.put("peerID", message.get("peerID").toString());
                peerMessage.put("peer", message.get("peer").toString());
                peerMessage.put("status", message.get("status").toString());

                _response.sendNotification(message, userIDs, "peer");
                response.remove("ownerID");
                response.remove("peerID");
                response.remove("peer");
                response.remove("status");
            }
            _response.sendResponse(response, header, "validateBranch");
        } catch (Exception e) {
            logger.error("Error in validate branch", e);
        }
    }

    /*
     * Request: 
     * {
     *      token: <jwt token>,
     *      branchTitle: <branch title>,
     *      userStory: <user story>,
     *      tasks: <list of tasks>,
     * }
     * 
     * Response:
     * {
     *      branchID: <created branchId>
     *      message: <message>,
     *      code: <status code>,
     *      method: <>
     * }
    */
    @MessageMapping("/createBranch")
    public void createBranch(@Payload Map<String, Object> message, SimpMessageHeaderAccessor header){
        try {
            Map<String, Object> response = _branch.sendAndReciveMessage(message, "create_branch");
            _response.sendResponse(response, header, "createBranch");            
        } catch (Exception e) {
            logger.error("Error in create branch", e);
        }
    }

    /*
     * Request: 
     * {
     *      token: <jwt token>,
     *      isClosed: <true or false>
     * }
     * 
     * Response:
     * {
     *      message: <message>,
     *      method: <>,
     *      code: <status code>,
     *      branches: [{
     *          branchID: <id of the branch>,
     *          branchTitle: <title of the branch>,
     *          userStory: <user story>,
     *          stages: ["DEV", "INTEG", "PROD"]
     *      }]
     * }
    */
    @MessageMapping("/getAllBranches")
    public void getAllBranches(@Payload Map<String, Object> message, SimpMessageHeaderAccessor header){
        try {
            Map<String, Object> response = _branch.sendAndReciveMessage(message, "get_all_branches");
            _response.sendResponse(response, header, "getAllBranches");      
        } catch (Exception e) {
            logger.error("Error in get all branches", e);
        }
    }

    /*
     * Request: 
     * {
     *      token: <jwt token>,
     *      branchID: <id of the branch>
     * }
     * 
     * Response:
     * {
     *      message: <message>,
     *      method: <>,
     *      code: <status code>,
     *      branche: {
     *          branchID: <id of the branch>,
     *          branchTitle: <title of the branch>,
     *          userStory: <user story>,
     *          stages: ["DEV", "INTEG", "PROD"],
     *          listOfTasks: [{
     *              taskID: <>,
     *              task: <>
     *          }],
     *          listOfMessages: [{
     *              messageID: <>,
     *              message: <>,
     *              postedBy: <>,
     *              postedOn: <>
     *          }],
     *          listOfPeers: [{
     *              peerID: <>,
     *              peer: <user name>,
     *              status: <pending, approved>
     *          }]
     *      }
     * }
    */
    @MessageMapping("/getBranchDetails")
    public void getBranchDetails(@Payload Map<String, Object> message, SimpMessageHeaderAccessor header){
        try {
            Map<String, Object> response = _branch.sendAndReciveMessage(message, "get_branch_details");
            _response.sendResponse(response, header, "getBranchDetails");        
        } catch (Exception e) {
            logger.error("Error in get branch details", e);
        }
    }

    /*
     * Request: 
     * {
     *      token: <jwt token>,
     *      branchID: <id of the branch>,
     *      task: <>
     * }
    */
    @MessageMapping("/addTask")
    public void addTask(@Payload Map<String, Object> message, SimpMessageHeaderAccessor header){
        try {
            /*
             * {
             *      status: <"success">,
             *      taskID: <>,
             *      task: <>
             * }
            */
            Map<String, Object> task =_branch.sendAndReciveMessage(message, "add_task");
            /*
             * {
             *      status: <"success">,
             *      users: [""]
             * }
            */
            Map<String, Object> response = _branch.sendAndReciveMessage(message, "get_users_for_branch");
            if(task.get("status").equals("success")){
                List<String> users = (List<String>) response.get("users");

                //async to send a message to associated users
                _response.sendNotification(task, users, "newTask");
            }
        } catch (Exception e) {
            logger.error("Error in add task", e);
        }
    }

    /*
     * Request: 
     * {
     *      token: <jwt token>,
     *      peerID: <id of the peer request>,
     *      status: <"APPROVE", "REJECT">
     * }
    */
    @MessageMapping("/peerAction")
    public void peerAction(@Payload Map<String, Object> message, SimpMessageHeaderAccessor headerAccessor){
        try {
            _branch.sendMessage(message, "peer_action");
        } catch (Exception e) {
            logger.error("Error in peer action", e);
        }
    }

    /*
     * Request: 
     * {
     *      token: <jwt token>,
     *      branchID: <id of the branch>,
     *      message: <>
     * }
    */
    @MessageMapping("/addMessage")
    public void addMessage(@Payload Map<String, Object> message, SimpMessageHeaderAccessor header){
        try {
            /*
             * {
             *      status: <"success">,
             *      mesageID: <>,
             *      message: <>,
             *      postedBy: <>,
             *      postedOn: <>
             * }
            */
            Map<String, Object> newMessage =_branch.sendAndReciveMessage(message, "add_message");
            /*
             * {
             *      status: <"success">,
             *      users: [""]
             * }
            */
            Map<String, Object> response = _branch.sendAndReciveMessage(message, "get_users_for_branch");
            if(response.get("status").equals("success")){
                List<String> users = (List<String>) response.get("users");

                //async to send a message to associated users
                _response.sendNotification(newMessage, users, "newMessage");
            }
        } catch (Exception e) {
            logger.error("Error in add message", e);
        }
    }

    /*
     * Request: 
     * {
     *      token: <jwt token>,
     *      branchID: <id of the branch>
     *      action: <"up", "down">
     * }
    */
    @MessageMapping("/moveBranch")
    public void moveBranch(@Payload Map<String, Object> message, SimpMessageHeaderAccessor header){
        try {
            _branch.sendMessage(message, "move_branch");      
        } catch (Exception e) {
            logger.error("Error in move branch", e);
        }
    }

    /*
     * Request: 
     * {
     *      token: <jwt token>,
     *      taskID: <id of the task>
     * }
    */
    @MessageMapping("/removeTask")
    public void removeTask(@Payload Map<String, Object> message, SimpMessageHeaderAccessor header){
        try {
            _branch.sendMessage(message, "remove_task");
            /*
             * {
             *      status: <"success">,
             *      users: [""]
             * }
            */
            Map<String, Object> response = _branch.sendAndReciveMessage(message, "get_users_for_branch");
             if(response.get("status").equals("success")){
                List<String> users = (List<String>) response.get("users");

                //async to send a message to associated users
                _response.sendNotification(message, users, "removeTask");
            }    
        } catch (Exception e) {
            logger.error("Error in remove task", e);
        }
    }

}
