package com.example.branch_tracker.branch_tracker.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.branch_tracker.branch_tracker.models.TaskCollection;

public interface TaskRepository extends MongoRepository<TaskCollection, String>{
    @Query(value = "{ 'branchID' : ?0 }", fields = "{ 'taskID': 1, 'task': 1 }")
    List<TaskCollection> findByBranchIDSelectFields(String branchID);
    void deleteByBranchID(String branchID);
}
