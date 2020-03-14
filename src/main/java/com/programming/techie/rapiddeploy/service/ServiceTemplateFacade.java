package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.mapper.ServiceTemplateMapper;
import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import com.programming.techie.rapiddeploy.repository.ServiceTemplateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceTemplateFacade {

    private final ServiceTemplateRepository serviceTemplateRepository;
    private final ServiceTemplateMapper serviceTemplateMapper;

    public String createServiceTemplate(ServiceTemplateDto serviceTemplatePayload) {
        return serviceTemplateRepository.save(serviceTemplateMapper.mapPayload(serviceTemplatePayload))
                .getGuid();
    }

    public List<ServiceTemplateDto> getAllServiceTemplates() {
        return serviceTemplateRepository.findAll()
                .stream()
                .map(serviceTemplateMapper::map)
                .collect(toList());
    }

    public void updateServiceTemplate(ServiceTemplateDto serviceTemplateDto) {
        ServiceTemplate existingServiceTemplate = findServiceTemplate(serviceTemplateDto.getGuid());
        ServiceTemplate updatedServiceTemplate = serviceTemplateMapper.mapPayload(serviceTemplateDto);

        updatedServiceTemplate.setId(existingServiceTemplate.getId());
        updatedServiceTemplate.setGuid(existingServiceTemplate.getGuid());
        log.info("Updated Service with GUID - " + serviceTemplateDto.getGuid());

        serviceTemplateRepository.save(updatedServiceTemplate);
    }

    public void deleteServiceTemplate(String guid) {
        serviceTemplateRepository.deleteByGuid(guid);
        log.info("Deleted Service with GUID - " + guid);
    }

    ServiceTemplate findServiceTemplate(String guid) {
        return serviceTemplateRepository.findByGuid(guid)
                .orElseThrow(() -> new RapidDeployException("Cannot find service template with GUID - " + guid));
    }

    public ServiceTemplateDto mapServiceTemplateToDto(ServiceTemplate serviceTemplate) {
        return serviceTemplateMapper.map(serviceTemplate);
    }
}
