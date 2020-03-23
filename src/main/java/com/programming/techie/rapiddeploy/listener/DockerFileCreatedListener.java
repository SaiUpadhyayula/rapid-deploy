package com.programming.techie.rapiddeploy.listener;

import com.programming.techie.rapiddeploy.events.DockerfileCreated;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import com.programming.techie.rapiddeploy.service.DockerContainerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@AllArgsConstructor
public class DockerFileCreatedListener {

    private final ApplicationRepository applicationRepository;
    private final DockerContainerService dockerContainerService;

    @EventListener
    public void handle(DockerfileCreated dockerfileCreated) {
        String imageId = dockerContainerService.buildDockerImage(dockerfileCreated.getFile());
        String appGuid = dockerfileCreated.getAppGuid();
        Application application = applicationRepository.findByGuid(appGuid)
                .orElseThrow(() -> new RapidDeployException("No Application found with GUID " + appGuid));
        String containerId = dockerContainerService.run(DockerContainerPayload.builder()
                .imageId(imageId)
                .environmentVariables(Collections.emptyList())
                .name("srv " + application.getName())
                .port(8080)
                .exposedPort(8080)
                .build()).getFirst();
        log.info("{}", dockerContainerService.inspectContainer(containerId));
        application.setContainerId(containerId);
        application.setImageId(imageId);
        applicationRepository.save(application);
    }
}
