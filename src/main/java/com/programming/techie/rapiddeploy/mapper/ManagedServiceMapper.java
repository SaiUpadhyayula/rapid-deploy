package com.programming.techie.rapiddeploy.mapper;

import com.programming.techie.rapiddeploy.dto.ManagedServiceResponse;
import com.programming.techie.rapiddeploy.model.ManagedService;
import com.programming.techie.rapiddeploy.model.ManagedServicePayload;
import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ManagedServiceMapper {

    @Mapping(target = "guid", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "serviceTemplate", source = "serviceTemplate")
    @Mapping(target = "environmentVariables", source = "managedServicePayload.environmentVariables")
    @Mapping(target = "name", source = "managedServicePayload.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logs", ignore = true)
    @Mapping(target = "containerId", ignore = true)
    ManagedService mapFromDto(ManagedServicePayload managedServicePayload, ServiceTemplate serviceTemplate);

    List<ManagedServiceResponse> mapToDtoList(List<ManagedService> managedServices);

    @Mapping(target = "serviceTemplateGuid", source = "serviceTemplate.name")
    @Mapping(target = "managedServiceGuid", source = "guid")
    ManagedServiceResponse mapToDto(ManagedService managedServices);
}
