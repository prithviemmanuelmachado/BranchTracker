package com.example.branch_tracker.branch_tracker.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.branch_tracker.branch_tracker.models.StageCollection;


public interface StageRepository extends MongoRepository<StageCollection, String>{
}
