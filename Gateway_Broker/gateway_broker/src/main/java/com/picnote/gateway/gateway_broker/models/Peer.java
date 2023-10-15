package com.picnote.gateway.gateway_broker.models;

import java.util.Date;
import java.util.UUID;

public class Peer {

    private UUID peerID;
    private String peer;
    private String status;
    private Date actionOn;
    private UUID branchID;

    public Peer(
        UUID peerID,
        String peer,
        String status,
        Date actionOn,
        UUID branchID
    ){
        this.peerID = peerID == null ? UUID.randomUUID() : peerID;
        this.peer = peer;
        this.status = status.isBlank() || status.isEmpty() ? "PENDING" : status;
        this.actionOn = actionOn == null ? new Date() : actionOn;
        this.branchID = branchID;
    }

}
