package com.picnote.gateway.gateway_broker.models;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class Branch {

    private UUID branchID;
    private String userStory;
    private boolean isClosed;
    private List<Task> tasks;
    
    public Branch(
        UUID branchID,
        String userStory,
        boolean isClosed,
        List<Task> tasks
    ){
        this.branchID = branchID == null ? UUID.randomUUID() : branchID;
        this.userStory = userStory;
        this.isClosed = isClosed;
        this.tasks = tasks;
    }
}