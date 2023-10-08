package com.picnote.gateway.gateway_broker.models;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.google.common.hash.Hashing;

import lombok.Data;

@Data
public class User {
    
    private UUID userID;
    private String name;
    private String email;
    private String password;

    public User(
        UUID userID,
        String name,
        String email,
        String password        
    ){
        this.userID = userID == null ? UUID.randomUUID() : userID;
        this.name = name;
        this.email = email;
        this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

}

