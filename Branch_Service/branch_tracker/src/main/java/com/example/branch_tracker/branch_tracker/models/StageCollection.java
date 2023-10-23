package com.example.branch_tracker.branch_tracker.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "stages")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StageCollection {
    @Id
    private String stageID;
    private String stage;
    private int Order;
}
