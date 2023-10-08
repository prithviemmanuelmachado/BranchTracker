package com.picnote.users.models;

import java.util.UUID;

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
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
