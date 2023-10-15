package com.picnote.users.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.picnote.users.consumers.UserConsumer;
import com.picnote.users.models.UserCollection;
import com.picnote.users.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private UserRepository _user;
    private Logger logger;

    public UserService(UserRepository _user){
        this._user = _user;
        this.logger = LoggerFactory.getLogger(UserConsumer.class);
    }
    
    public Map<String, Object> addUser(UserCollection u){
        Map<String, Object> response = new HashMap<>();
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

    public Map<String, Object> login(UserCollection u){
        Map<String, Object> response = new HashMap<>();
        try {
            if(_user.existsByEmail(u.getEmail())){
                if(_user.existsByPassword(u.getPassword())){
                    Date now = new Date();
                    Date expiryDate = new Date(now.getTime() + expirationMs);

                    UserCollection searchedUser = _user.findByEmail(u.getEmail()).getFirst();
                    Map<String, String> obj = new HashMap<>();
                    obj.put("userID", searchedUser.getUserID());
                    obj.put("name", searchedUser.getName());
                    obj.put("email", searchedUser.getEmail());

                    String token = Jwts.builder()
                        .setSubject(obj.toString())
                        .setIssuedAt(now)
                        .setExpiration(expiryDate)
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact();

                    response.put("token", token);
                    response.put("status", "success");
                } else {
                    response.put("message", "Invalid password");
                    response.put("status", "invalid");
                }                
            } else {
                response.put("message", "Invalid email");
                response.put("status", "invalid");
            }
            return response;            
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("error in login service", e);
            response.put("message", "A problem occured when trying to login. Please try agian later");
            response.put("status", "error");
            return response;
        }
    }
} 
