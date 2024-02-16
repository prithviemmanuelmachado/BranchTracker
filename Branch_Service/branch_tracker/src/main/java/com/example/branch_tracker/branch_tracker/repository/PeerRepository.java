package com.example.branch_tracker.branch_tracker.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.branch_tracker.branch_tracker.models.PeerCollection;

public interface PeerRepository extends MongoRepository<PeerCollection, String>{
    List<PeerCollection> findByBranchIDAndIsOwnerTrue(String branchID);

    @Query(value = "{ 'branchID' : ?0, 'peerUserID': { '$ne' :  ?1} }", fields = "{ 'peerUserID': 1 }")
    List<PeerCollection> findByBranchIDAndNotUserIDSelectFields(String branchID, String peerUserID);

    @Query(value = "{ 'branchID' : ?0, 'isOwner': false, 'status': { '$ne' : 'REJECT' } }", fields = "{ 'peerID': 1, 'peerName': 1, 'status': 1 }")
    List<PeerCollection> findByBranchIDAndIsOwnerFalseSelectFields(String branchID);

    @Query(value = "{ 'branchID' : ?0, 'userID' : ?1, 'isOwner': false }", fields = "{ 'peerID': 1 }")
    List<PeerCollection> findByBranchIDAndUserIDAndIsOwnerTrueSelectFields(String branchID, String userID);

    @Query(value = "{ 'branchID' : ?0, 'userID' : ?1, 'isOwner': false, 'status': { '$eq' : 'PENDING' } }", fields = "{ 'peerID': 1 }")
    List<PeerCollection> findByBranchIDAndUserIDAndIsOwnerFalseSelectFields(String branchID, String userID);

    // value is the condition to match, ?0 is the firts param passed, fields is the data returned
    @Query(value = "{ 'peerUserID' : ?0 }", fields = "{ 'branchID' : 1 }")
    List<PeerCollection> findBranchIDsByPeerUserIDSelectFields(String peerUserID);

    void deleteByBranchID(String branchID);
}
