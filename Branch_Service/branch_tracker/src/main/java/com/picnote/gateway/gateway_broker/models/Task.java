package com.picnote.gateway.gateway_broker.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Task {

    private UUID taskID;
    private String task;

    public Task(
        @JsonProperty("taskID")
        UUID taskID,
        @JsonProperty("task")
        String task
    ){
        this.taskID = taskID;
        this.task = task;
    }

}
