package com.picnote.gateway.gateway_broker.models;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class Task {

    private UUID taskID;
    private String task;
    private String createdBy;
    private Date createdOn;

    public Task(
        UUID taskID,
        String task,
        String createdBy,
        Date createdOn
    ){
        this.taskID = taskID == null ? UUID.randomUUID() : taskID;
        this.task = task;
        this.createdBy = createdBy;
        this.createdOn = createdOn == null ? new Date() : createdOn;
    }

}
