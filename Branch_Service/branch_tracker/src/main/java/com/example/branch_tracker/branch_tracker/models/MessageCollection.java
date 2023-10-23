package com.example.branch_tracker.branch_tracker.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "messages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCollection {
    @Id
    private String messageID;
    private String message;
    private String branchID;
    private String postedBy;
    private Date postedOn;
}
