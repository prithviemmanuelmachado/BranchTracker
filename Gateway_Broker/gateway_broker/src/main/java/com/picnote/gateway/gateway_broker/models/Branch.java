package com.picnote.gateway.gateway_broker.models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class Branch {

    private UUID branchID;
    private String userStory;
    private boolean isClosed;
    private String actionBy;
    private Date actionOn;
    private List<Task> tasks;
    
    public Branch(
        UUID branchID,
        String userStory,
        boolean isClosed,
        String actionBy,
        Date actionOn,
        List<Task> tasks
    ){
        this.branchID = branchID == null ? UUID.randomUUID() : branchID;
        this.userStory = userStory;
        this.isClosed = isClosed;
        this.actionBy = actionBy;
        this.actionOn = actionOn == null ? new Date() : actionOn;
        this.tasks = tasks;
    }
}
