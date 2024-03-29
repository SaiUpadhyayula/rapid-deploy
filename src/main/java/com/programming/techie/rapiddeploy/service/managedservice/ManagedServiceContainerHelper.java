package com.programming.techie.rapiddeploy.service.managedservice;

import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import com.programming.techie.rapiddeploy.model.ManagedService;
import com.programming.techie.rapiddeploy.service.docker.DockerContainerService;
import com.programming.techie.rapiddeploy.service.docker.DockerImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.RAPID_DEPLOY_SERVICE_PREFIX;

@Service
@AllArgsConstructor
@Slf4j
public class ManagedServiceContainerHelper {

    private final DockerContainerService dockerContainerService;
    private final DockerImageService dockerImageService;

    Pair<String, String> startManagedServiceContainerWithPull(ManagedService managedService) {
        dockerImageService.pullImage(managedService.getServiceTemplate().getImageName(), managedService.getServiceTemplate().getTagName());
        return startManagedServiceContainer(managedService);
    }

    Pair<String, String> startManagedServiceContainer(ManagedService managedService) {
        String imageId = dockerImageService.getImageId(managedService.getServiceTemplate().getImageName());
        return dockerContainerService.run(DockerContainerPayload.builder()
                .imageId(imageId)
                .environmentVariables(managedService.getEnvironmentVariables())
                .name(RAPID_DEPLOY_SERVICE_PREFIX + managedService.getName())
                .port(managedService.getServiceTemplate().getPortNumber())
                .volumeList(Collections.emptyList())
                .exposedPort(managedService.getServiceTemplate().getPortNumber())
                .build());
    }

    void stopManagedServiceContainer(ManagedService managedService) {
        dockerContainerService.stop(managedService.getContainerId());
    }

    String inspectManagedServiceContainer(ManagedService managedService) {
        return dockerContainerService.inspectContainer(managedService.getContainerId());
    }
}
