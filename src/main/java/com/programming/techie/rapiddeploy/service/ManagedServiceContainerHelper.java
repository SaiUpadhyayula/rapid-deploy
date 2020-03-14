package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.model.ManagedService;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ManagedServiceContainerHelper {

    private final DockerContainerService dockerContainerService;

    Pair<String, String> startManagedServiceContainerWithPull(ManagedService managedService) {
        dockerContainerService.pullImage(managedService.getServiceTemplate().getImageName(), managedService.getServiceTemplate().getTagName());
        return startManagedServiceContainer(managedService);
    }

    Pair<String, String> startManagedServiceContainer(ManagedService managedService) {
        String imageId = dockerContainerService.getImageId(managedService.getServiceTemplate().getImageName());
        return dockerContainerService.run(imageId, managedService.getEnvironmentVariables(), managedService.getName());
    }

    void stopManagedServiceContainer(ManagedService managedService) {
        dockerContainerService.stop(managedService.getContainerId());
    }

    String inspectManagedServiceContainer(ManagedService managedService) {
        return dockerContainerService.inspectContainer(managedService.getContainerId());
    }
}
