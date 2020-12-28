package com.programming.techie.rapiddeploy.mapper;

import com.programming.techie.rapiddeploy.model.ManagedService;
import com.programming.techie.rapiddeploy.model.ManagedServicePayload;
import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ManagedServiceMapper {

    @Mapping(target = "guid", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "serviceTemplate", source = "serviceTemplate")
    @Mapping(target = "environmentVariables", source = "managedServicePayload.environmentVariables")
    @Mapping(target = "name", source = "managedServicePayload.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "containerId", ignore = true)
    ManagedService map(ManagedServicePayload managedServicePayload, ServiceTemplate serviceTemplate);
}
