package com.picnote.gateway.gateway_broker.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Message {

    private UUID messageID;
    private String message;
    private UUID branchID;

    public Message(
        @JsonProperty("messageID")
        UUID messageID,
        @JsonProperty("message")
        String message,
        @JsonProperty("branchID")
        UUID branchID
    ){
        this.messageID = messageID;
        this.message = message;
        this.branchID = branchID;
    }
    
}