package com.programming.techie.rapiddeploy.repository;

import com.programming.techie.rapiddeploy.model.ManagedService;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ManagedServiceRepository extends MongoRepository<ManagedService, String> {
    Optional<ManagedService> findByGuid(String managedServiceGuid);
}
