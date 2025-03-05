package com.journal.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.journal.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId>{
	
	public User findByUsername(String username);
	
	public void deleteByUsername(String username);

}
