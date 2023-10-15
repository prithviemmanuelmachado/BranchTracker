package com.picnote.gateway.gateway_broker.models;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Branch {

    private UUID branchID;
    private String userStory;
    private boolean isClosed;
    private List<Task> tasks;
    
    public Branch(
        @JsonProperty("branchID")
        UUID branchID,
        @JsonProperty("userStory")
        String userStory,
        @JsonProperty("isClosed")
        boolean isClosed,
        @JsonProperty("tasks")
        List<Task> tasks
    ){
        this.branchID = branchID;
        this.userStory = userStory;
        this.isClosed = isClosed;
        this.tasks = tasks;
    }
}
