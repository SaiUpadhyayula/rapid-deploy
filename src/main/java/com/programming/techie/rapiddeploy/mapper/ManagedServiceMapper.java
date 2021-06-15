package com.programming.techie.rapiddeploy.mapper;

import com.programming.techie.rapiddeploy.model.ManagedService;
import com.programming.techie.rapiddeploy.model.ManagedServicePayload;
import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ManagedServiceMapper {
    public ManagedService map(ManagedServicePayload managedServicePayload, ServiceTemplate serviceTemplate) {
        return ManagedService.builder()
                .guid(UUID.randomUUID().toString())
                .serviceTemplate(serviceTemplate)
                .environmentVariables(managedServicePayload.getEnvironmentVariables())
                .name(managedServicePayload.getName())
                .build();
    }
}
