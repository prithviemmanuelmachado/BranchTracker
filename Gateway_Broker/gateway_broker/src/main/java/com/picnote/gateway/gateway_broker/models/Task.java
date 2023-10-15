package com.picnote.gateway.gateway_broker.models;

import java.util.UUID;

import lombok.Data;

@Data
public class Task {

    private UUID taskID;
    private String task;

    public Task(
        UUID taskID,
        String task
    ){
        this.taskID = taskID == null ? UUID.randomUUID() : taskID;
        this.task = task;
    }

}
