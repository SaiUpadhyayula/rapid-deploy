package com.programming.techie.rapiddeploy.mapper;

import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServiceTemplateMapper {
    public ServiceTemplateDto map(ServiceTemplate serviceTemplate) {
        return ServiceTemplateDto.builder()
                .name(serviceTemplate.getName())
                .guid(serviceTemplate.getGuid())
                .description(serviceTemplate.getDescription())
                .imageName(serviceTemplate.getImageName())
                .tagName(serviceTemplate.getTagName())
                .portNumber(serviceTemplate.getPortNumber())
                .build();
    }

    public ServiceTemplate mapPayload(ServiceTemplateDto serviceTemplatePayload) {
        return ServiceTemplate.builder()
                .name(serviceTemplatePayload.getName())
                .guid(UUID.randomUUID().toString())
                .description(serviceTemplatePayload.getDescription())
                .imageName(serviceTemplatePayload.getImageName())
                .tagName(serviceTemplatePayload.getTagName())
                .portNumber(serviceTemplatePayload.getPortNumber())
                .build();
    }
}
