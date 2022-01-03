package com.programming.techie.rapiddeploy.service.application;

import com.github.dockerjava.api.model.MountType;
import com.programming.techie.rapiddeploy.dto.YamlParsingCompleted;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload.Volume;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import com.programming.techie.rapiddeploy.service.docker.DockerContainerService;
import com.programming.techie.rapiddeploy.service.docker.DockerImageService;
import com.programming.techie.rapiddeploy.service.nginx.NginxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;

import static com.programming.techie.rapiddeploy.model.ApplicationState.STARTED;
import static com.programming.techie.rapiddeploy.model.BuildState.IN_PROGRESS;
import static com.programming.techie.rapiddeploy.model.BuildState.SUCCESS;
import static com.programming.techie.rapiddeploy.service.docker.BuildCacheVolumeConstants.BUILD_CACHE_VOLUMES_MAP;
import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.RAPID_DEPLOY_SERVICE_PREFIX;
import static org.springframework.data.util.Pair.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationOrchestrator {

    private final ApplicationRepository applicationRepository;
    private final DockerImageService dockerImageService;
    private final DockerContainerService dockerContainerService;
    private final NginxService nginxService;

    public void handle(File file, YamlParsingCompleted parsedYaml) {
        Application application = applicationRepository.findByGuid(parsedYaml.getAppGuid())
                .orElseThrow(() -> new RapidDeployException("No Application found with GUID " + parsedYaml.getAppGuid()));
        Pair<String, Application> pair = buildImage(file, application);
        String imageId = pair.getFirst();
        application = pair.getSecond();

        String containerId = dockerContainerService.run(DockerContainerPayload.builder()
                .imageId(imageId)
                .environmentVariables(Collections.emptyList())
                .name(RAPID_DEPLOY_SERVICE_PREFIX + application.getName())
                .port(application.getPort())
                .exposedPort(application.getPort())
                .volumeList(Collections.singletonList(new Volume(application.getName(),
                        BUILD_CACHE_VOLUMES_MAP.get(parsedYaml.getManifestDefinition().getLanguage()),
                        MountType.VOLUME)))
                .build()).getFirst();
        nginxService.createConfigFile(RAPID_DEPLOY_SERVICE_PREFIX + application.getName(), application.getPort());
        nginxService.start();
        application.setContainerId(containerId);
        application.setApplicationState(STARTED);
        applicationRepository.save(application);
    }

    private Pair<String, Application> buildImage(File file, Application application) {
        application.setBuildState(IN_PROGRESS);
        applicationRepository.save(application);
        String imageId = dockerImageService.buildDockerImage(file);
        application.setBuildState(SUCCESS);
        application.setImageId(imageId);
        applicationRepository.save(application);
        return of(imageId, application);
    }
}
