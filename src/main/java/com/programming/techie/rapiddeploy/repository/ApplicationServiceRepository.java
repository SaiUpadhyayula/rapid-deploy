package com.programming.techie.rapiddeploy.repository;

import com.programming.techie.rapiddeploy.model.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationServiceRepository extends MongoRepository<Service, String> {
}
