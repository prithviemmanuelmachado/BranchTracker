package com.picnote.gateway.gateway_broker.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Peer {

    private UUID peerID;
    private String status;
    private UUID branchID;

    public Peer(
        @JsonProperty("peerID")
        UUID peerID,
        @JsonProperty("status")
        String status,
        @JsonProperty("branchID")
        UUID branchID
    ){
        this.peerID = peerID;
        this.status = status;
        this.branchID = branchID;
    }

}
