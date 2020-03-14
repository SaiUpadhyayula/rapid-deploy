package com.programming.techie.rapiddeploy.mapper;

import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceTemplateMapper {
    ServiceTemplateDto map(ServiceTemplate serviceTemplate);

    @Mapping(target = "guid", expression = "java(java.util.UUID.randomUUID().toString())")
    ServiceTemplate mapPayload(ServiceTemplateDto serviceTemplatePayload);
}
