package com.programming.techie.rapiddeploy.service.managedservice;

import com.programming.techie.rapiddeploy.dto.ManagedServiceResponse;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.mapper.ManagedServiceMapper;
import com.programming.techie.rapiddeploy.model.ManagedService;
import com.programming.techie.rapiddeploy.model.ManagedServicePayload;
import com.programming.techie.rapiddeploy.model.ServiceTemplate;
import com.programming.techie.rapiddeploy.payload.ServiceTemplateDto;
import com.programming.techie.rapiddeploy.repository.ManagedServiceRepository;
import com.programming.techie.rapiddeploy.service.servicetemplate.ServiceTemplateFacade;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ManagedServiceFacade {

    private final ServiceTemplateFacade serviceTemplateFacade;
    private final ManagedServiceMapper managedServiceMapper;
    private final ManagedServiceRepository managedServiceRepository;
    private final ManagedServiceContainerHelper managedServiceContainerHelper;


    public ServiceTemplateDto initManagedService(ServiceTemplateDto serviceTemplatePayload) {
        ServiceTemplate serviceTemplate = serviceTemplateFacade.findServiceTemplate(serviceTemplatePayload.getGuid());
        return serviceTemplateFacade.mapServiceTemplateToDto(serviceTemplate);
    }

    public String createManagedService(ManagedServicePayload managedServicePayload) {
        ServiceTemplate serviceTemplate = serviceTemplateFacade.findServiceTemplate(managedServicePayload.getServiceTemplateGuid());
        ManagedService managedService = managedServiceMapper.map(managedServicePayload, serviceTemplate);
        Pair<String, String> containerIdNamePair = managedServiceContainerHelper.startManagedServiceContainerWithPull(managedService);
        managedService.setContainerId(containerIdNamePair.getFirst());
        managedServiceRepository.save(managedService);
        return containerIdNamePair.getSecond();
    }

    public void updateManagedService(ManagedServicePayload managedServicePayload) {
        ManagedService existingManagedService = findManagedServiceByGuid(managedServicePayload.getManagedServiceGuid());

        ServiceTemplate serviceTemplate = serviceTemplateFacade.findServiceTemplate(managedServicePayload.getServiceTemplateGuid());
        ManagedService managedService = managedServiceMapper.map(managedServicePayload, serviceTemplate);
        managedService.setId(existingManagedService.getId());
        managedService.setGuid(existingManagedService.getGuid());

        managedServiceContainerHelper.stopManagedServiceContainer(managedService);
        managedServiceContainerHelper.startManagedServiceContainer(managedService);
        managedServiceRepository.save(managedService);
    }

    public void startManagedService(String managedServiceGuid) {
        ManagedService managedService = findManagedServiceByGuid(managedServiceGuid);
        managedServiceContainerHelper.startManagedServiceContainer(managedService);
//        inspect(containerId);
    }


    public String inspect(String managedServiceGuid) {
        ManagedService managedService = findManagedServiceByGuid(managedServiceGuid);
        return managedServiceContainerHelper.inspectManagedServiceContainer(managedService);
    }

    private ManagedService findManagedServiceByGuid(String managedServiceGuid) {
        return managedServiceRepository.findByGuid(managedServiceGuid)
                .orElseThrow(() -> new RapidDeployException("Cannot find Managed Service with GUID - " + managedServiceGuid));
    }

    public List<ManagedServiceResponse> getAll() {
        return managedServiceMapper.mapToDtoList(managedServiceRepository.findAll());
    }
}
