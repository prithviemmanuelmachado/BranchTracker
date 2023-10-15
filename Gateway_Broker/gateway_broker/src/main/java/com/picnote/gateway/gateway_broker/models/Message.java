package com.picnote.gateway.gateway_broker.models;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class Message {

    private UUID messageID;
    private String message;
    private String postedBy;
    private Date postedOn;
    private UUID branchID;

    public Message(
        UUID messageID,
        String message,
        String postedBy,
        Date postedOn,
        UUID branchID
    ){
        this.messageID = messageID == null ? UUID.randomUUID() : messageID;
        this.message = message;
        this.postedBy = postedBy;
        this.postedOn = postedOn == null ? new Date() : postedOn;
        this.branchID = branchID;
    }
    
}
