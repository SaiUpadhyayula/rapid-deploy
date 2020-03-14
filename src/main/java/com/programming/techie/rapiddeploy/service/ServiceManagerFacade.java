package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.mapper.ManagedServiceMapper;
import com.programming.techie.rapiddeploy.mapper.ServiceTemplateMapper;
import com.programming.techie.rapiddeploy.model.ManagedService;
import com.programming.techie.rapiddeploy.model.ManagedServicePayload;
import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import com.programming.techie.rapiddeploy.repository.ManagedServiceRepository;
import com.programming.techie.rapiddeploy.repository.ServiceTemplateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceManagerFacade {

    private final ServiceTemplateRepository serviceTemplateRepository;
    private final ServiceTemplateMapper serviceTemplateMapper;
    private final ManagedServiceMapper managedServiceMapper;
    private final ManagedServiceRepository managedServiceRepository;
    private final ServiceFactory serviceFactory;

    public ServiceTemplateDto initManagedService(ServiceTemplateDto serviceTemplatePayload) {
        ServiceTemplate serviceTemplate = findServiceTemplate(serviceTemplatePayload.getGuid());
        return serviceTemplateMapper.map(serviceTemplate);
    }


    public void createManagedService(ManagedServicePayload managedServicePayload) {
        ServiceTemplate serviceTemplate = findServiceTemplate(managedServicePayload.getServiceTemplateGuid());
        ManagedService managedService = managedServiceMapper.map(managedServicePayload, serviceTemplate);
        serviceFactory.pullImage(managedService);
        String containerId = serviceFactory.startManagedServiceContainer(managedService);
        managedService.setContainerId(containerId);
        managedServiceRepository.save(managedService);
    }

    public void updateManagedService(ManagedServicePayload managedServicePayload) {
        ManagedService existingManagedService = managedServiceRepository.findByGuid(managedServicePayload.getManagedServiceGuid())
                .orElseThrow(() -> new RapidDeployException("Cannot find Managed Service with GUID - " + managedServicePayload.getManagedServiceGuid()));

        ServiceTemplate serviceTemplate = findServiceTemplate(managedServicePayload.getServiceTemplateGuid());
        ManagedService managedService = managedServiceMapper.map(managedServicePayload, serviceTemplate);
        managedService.setId(existingManagedService.getId());
        managedService.setGuid(existingManagedService.getGuid());

        serviceFactory.stopManagedServiceContainer(managedService);
        serviceFactory.startManagedServiceContainer(managedService);
        managedServiceRepository.save(managedService);
    }

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

    private ServiceTemplate findServiceTemplate(String guid) {
        return serviceTemplateRepository.findByGuid(guid)
                .orElseThrow(() -> new RapidDeployException("Cannot find service template with GUID - " + guid));
    }
}
