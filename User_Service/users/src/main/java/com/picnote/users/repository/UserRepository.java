package com.picnote.users.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.picnote.users.models.UserCollection;

public interface UserRepository extends MongoRepository<UserCollection, String>{
    boolean existsByEmail(String email);    
}
