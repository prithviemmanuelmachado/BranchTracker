package com.picnote.gateway.gateway_broker.models;

import java.util.UUID;

import lombok.Data;

@Data
public class Message {

    private UUID messageID;
    private String message;
    private UUID branchID;

    public Message(
        UUID messageID,
        String message,
        UUID branchID
    ){
        this.messageID = messageID == null ? UUID.randomUUID() : messageID;
        this.message = message;
        this.branchID = branchID;
    }
    
}