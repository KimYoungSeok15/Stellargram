package com.instargram101.chat.repository;

import com.instargram101.chat.entity.AutoSequence;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountersRepository extends MongoRepository<AutoSequence,String> {

    Optional<AutoSequence> findById(String id);
}
