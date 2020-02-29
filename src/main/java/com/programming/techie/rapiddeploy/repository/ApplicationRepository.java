package com.programming.techie.rapiddeploy.repository;

import com.programming.techie.rapiddeploy.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    Optional<Application> findByName(String appName);
}
