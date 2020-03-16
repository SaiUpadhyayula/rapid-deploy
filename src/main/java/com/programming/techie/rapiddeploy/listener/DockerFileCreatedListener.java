package com.programming.techie.rapiddeploy.listener;

import com.programming.techie.rapiddeploy.events.DockerfileCreated;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.Application;
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
        String containerId = dockerContainerService.run(imageId, Collections.emptyList(), "test").getFirst();
        log.info("{}", dockerContainerService.inspectContainer(containerId));
        String appGuid = dockerfileCreated.getAppGuid();
        Application application = applicationRepository.findByGuid(appGuid)
                .orElseThrow(() -> new RapidDeployException("No Application found with GUID " + appGuid));
        application.setContainerId(containerId);
        application.setImageId(imageId);
        applicationRepository.save(application);
    }
}
