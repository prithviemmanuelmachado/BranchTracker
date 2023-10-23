package com.example.branch_tracker.branch_tracker.models;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "branches")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchCollection {
    @Id
    private String branchID;
    private String branchTitle;
    private List<String> stages;
    private String userStory;
    private boolean isClosed;
    private String createdBy;
    private String createdByUserID;
    private Date createdOn;
    private Date modifiedOn;
}
