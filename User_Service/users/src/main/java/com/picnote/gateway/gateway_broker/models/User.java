package com.picnote.gateway.gateway_broker.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class User {
    private UUID userID;
    private String name;
    private String email;
    private String password;

    public User(
        @JsonProperty("userID")
        UUID userID,
        @JsonProperty("name")
        String name,
        @JsonProperty("email")
        String email,
        @JsonProperty("password")
        String password        
    ){
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
