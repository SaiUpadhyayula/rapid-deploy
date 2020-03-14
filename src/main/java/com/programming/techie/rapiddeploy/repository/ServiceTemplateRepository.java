package com.programming.techie.rapiddeploy.repository;

import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceTemplateRepository extends MongoRepository<ServiceTemplate, String> {
    Optional<ServiceTemplate> findByGuid(String guid);

    void deleteByGuid(String guid);
}
