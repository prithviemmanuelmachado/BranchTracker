package com.example.branch_tracker.branch_tracker.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCollection {
    @Id
    private String taskID;
    private String task;
    private String branchID;
    private String createdBy;
    private Date createdOn;
}
