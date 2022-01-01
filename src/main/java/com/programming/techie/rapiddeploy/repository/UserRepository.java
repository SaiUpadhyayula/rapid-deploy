package com.programming.techie.rapiddeploy.repository;

import com.programming.techie.rapiddeploy.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserName(String username);
}
