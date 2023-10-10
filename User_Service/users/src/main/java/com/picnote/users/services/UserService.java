package com.picnote.users.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.picnote.users.consumers.UserConsumer;
import com.picnote.users.models.UserCollection;
import com.picnote.users.repository.UserRepository;

@Service
public class UserService {

    private UserRepository _user;
    private Logger logger;

    public UserService(UserRepository _user){
        this._user = _user;
        this.logger = LoggerFactory.getLogger(UserConsumer.class);
    }
    
    public java.util.Map<String, String> addUser(UserCollection u){
        Map<String, String> response = new HashMap<>();
        try {
            if(_user.existsByEmail(u.getEmail())){
                response.put("message", "User already exists. Please try another email or proceed to login");
                response.put("status", "exists");
            }else {
                _user.save(u);
                response.put("message", "User created successfully");
                response.put("status", "inserted");
            }
            return response;            
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("error in add user service", e);
            response.put("message", "A problem occured when trying to add this user. Please try agian later");
            response.put("status", "error");
            return response;
        }
    }
} 
