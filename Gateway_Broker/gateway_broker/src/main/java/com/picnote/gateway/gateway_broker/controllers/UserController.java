package com.picnote.gateway.gateway_broker.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picnote.gateway.gateway_broker.models.User;
import com.picnote.gateway.gateway_broker.publisher.UserPublisher;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserPublisher _user;

    public UserController(UserPublisher _user){
        this._user = _user;
    }
    
    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody User user){
        try {
            Map<String,String> message = _user.sendAndReciveMessage(user, "register");
            if(message.size() == 0){
                throw new Exception();
            } else if (message.get("status").equals("inserted")){
                return new ResponseEntity<Object>(message, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "A problem occured when trying to add this user. Please try agian later");
            return new ResponseEntity<Object>(response , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user){
        try {
            Map<String,String> message = _user.sendAndReciveMessage(user, "login");
            if(message.size() == 0){
                throw new Exception();
            } else if (message.get("status").equals("success")){
                return new ResponseEntity<Object>(message, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "A problem occured when trying to add this user. Please try agian later");
            return new ResponseEntity<Object>(response , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
