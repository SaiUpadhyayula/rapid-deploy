package com.programming.techie.rapiddeploy.service.application;

import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import com.programming.techie.rapiddeploy.service.docker.DockerContainerService;
import com.programming.techie.rapiddeploy.service.docker.DockerImageService;
import com.programming.techie.rapiddeploy.service.nginx.NginxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collections;

import static com.programming.techie.rapiddeploy.model.ApplicationState.STARTED;
import static com.programming.techie.rapiddeploy.model.BuildState.IN_PROGRESS;
import static com.programming.techie.rapiddeploy.model.BuildState.SUCCESS;
import static org.springframework.data.util.Pair.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationOrchestrator {

    private final ApplicationRepository applicationRepository;
    private final DockerImageService dockerImageService;
    private final DockerContainerService dockerContainerService;
    private final NginxService nginxService;

    public void handle(File file, String appGuid) {
        Application application = applicationRepository.findByGuid(appGuid)
                .orElseThrow(() -> new RapidDeployException("No Application found with GUID " + appGuid));
        Pair<String, Application> pair = buildImage(file, application);
        String imageId = pair.getFirst();
        application = pair.getSecond();
//        dockerPushService.push(imageId);
        String containerId = dockerContainerService.run(DockerContainerPayload.builder()
                .imageId(imageId)
                .environmentVariables(Collections.emptyList())
                .name(application.getName())
                .port(8080)
                .exposedPort(8081)
                .build()).getFirst();
        nginxService.start(false);
        application.setContainerId(containerId);
        application.setImageId(imageId);
        application.setApplicationState(STARTED);
        applicationRepository.save(application);
    }

    private Pair<String, Application> buildImage(File file, Application application) {
        application.setBuildState(IN_PROGRESS);
        applicationRepository.save(application);
        String imageId = dockerImageService.buildDockerImage(file);
        application.setBuildState(SUCCESS);
        applicationRepository.save(application);
        return of(imageId, application);
    }
}
