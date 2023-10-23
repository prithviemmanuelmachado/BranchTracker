package com.example.branch_tracker.branch_tracker.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "peers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeerCollection {
    @Id
    private String peerID;
    private String peerName;
    private String peerUserID;
    private String status;
    private String branchID;
    private boolean isOwner;
    private Date acitonOn;
}
