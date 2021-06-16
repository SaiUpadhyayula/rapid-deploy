package com.programming.techie.rapiddeploy.mapper;

import com.programming.techie.rapiddeploy.dto.ManagedServiceResponse;
import com.programming.techie.rapiddeploy.model.ManagedService;
import com.programming.techie.rapiddeploy.model.ManagedServicePayload;
import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ManagedServiceMapper {
    public ManagedService mapFromDto(ManagedServicePayload managedServicePayload, ServiceTemplate serviceTemplate) {
        return ManagedService.builder()
                .guid(UUID.randomUUID().toString())
                .serviceTemplate(serviceTemplate)
                .environmentVariables(managedServicePayload.getEnvironmentVariables())
                .name(managedServicePayload.getName())
                .build();
    }

    public List<ManagedServiceResponse> mapToDtoList(List<ManagedService> managedServices) {
        return managedServices.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    ManagedServiceResponse mapToDto(ManagedService managedServices) {
        return ManagedServiceResponse.builder()
                .managedServiceGuid(managedServices.getGuid())
                .name(managedServices.getName())
                .serviceTemplateGuid(managedServices.getServiceTemplate().getGuid())
                .environmentVariables(managedServices.getEnvironmentVariables())
                .build();
    }
}
