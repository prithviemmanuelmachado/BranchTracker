package com.picnote.gateway.gateway_broker.models;

import java.util.UUID;

import lombok.Data;

@Data
public class Peer {

    private UUID peerID;
    private String status;
    private UUID branchID;

    public Peer(
        UUID peerID,
        String status,
        UUID branchID
    ){
        this.peerID = peerID == null ? UUID.randomUUID() : peerID;
        this.status = status.isBlank() || status.isEmpty() ? "PENDING" : status;
        this.branchID = branchID;
    }

}