package com.example.branch_tracker.branch_tracker.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.branch_tracker.branch_tracker.models.MessageCollection;
import java.util.List;

public interface MessageRepository extends MongoRepository<MessageCollection, String>{
    List<MessageCollection> findByBranchID(String branchID, Sort sort);
}
