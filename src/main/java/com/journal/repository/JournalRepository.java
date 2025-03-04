package com.journal.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.journal.entity.JournalEntry;

@Repository
public interface JournalRepository extends MongoRepository<JournalEntry, ObjectId>{

}
