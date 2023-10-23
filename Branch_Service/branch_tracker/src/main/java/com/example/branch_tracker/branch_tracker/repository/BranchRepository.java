package com.example.branch_tracker.branch_tracker.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.branch_tracker.branch_tracker.models.BranchCollection;
import java.util.List;

public interface BranchRepository extends MongoRepository<BranchCollection, String>{
    List<BranchCollection> findByBranchTitle(String branchTitle);
    List<BranchCollection> findByBranchID(String branchID);
    List<BranchCollection> findByBranchIDInAndIsClosed(List<String> branchIDs, boolean isClosed, Sort sort);
    void deleteByBranchID(String branchID);
    
}
