package com.programming.techie.rapiddeploy.service.application;

import com.github.dockerjava.api.model.MountType;
import com.programming.techie.rapiddeploy.exceptions.ApplicationAlreadyExistsException;
import com.programming.techie.rapiddeploy.exceptions.ApplicationNotFoundException;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.mapper.ApplicationMapper;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload.Volume;
import com.programming.techie.rapiddeploy.model.EnvironmentVariables;
import com.programming.techie.rapiddeploy.payload.ApplicationPayload;
import com.programming.techie.rapiddeploy.payload.ApplicationResponse;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import com.programming.techie.rapiddeploy.service.docker.DockerContainerService;
import com.programming.techie.rapiddeploy.service.nginx.NginxService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.programming.techie.rapiddeploy.service.docker.BuildCacheVolumeConstants.BUILD_CACHE_VOLUMES_MAP;
import static com.programming.techie.rapiddeploy.util.RapidDeployConstants.RAPID_DEPLOY_SERVICE_PREFIX;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationService {

    public static final String NO_APP_FOUND_GUID = "No Application found with guid - ";
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final DockerContainerService dockerContainerService;
    private final NginxService nginxService;

    public ApplicationResponse create(ApplicationPayload applicationPayload) {
        String applicationName = applicationPayload.getApplicationName();
        Application application = Application.builder()
                .name(applicationName)
                .guid(UUID.randomUUID().toString())
                .environmentVariables(Collections.emptyList())
                .port(applicationPayload.getPort())
                .build();
        applicationRepository.findByName(applicationPayload.getApplicationName())
                .ifPresent(el -> {
                    throw new ApplicationAlreadyExistsException("Application Name - " + applicationName + " already in use, Please provide another name");
                });
        applicationRepository.save(application);
        return applicationMapper.map(application);
    }

    public List<ApplicationResponse> getAll() {
        return applicationRepository.findAll()
                .stream()
                .map(applicationMapper::map)
                .collect(Collectors.toList());
    }

    public ApplicationResponse getOne(String guid) {
        return findAppByGuid(guid);
    }

    public void delete(String guid) {
        applicationRepository.deleteByGuid(guid);
    }

    public ApplicationResponse update(ApplicationPayload applicationPayload) {
        var application = applicationRepository.findByGuid(applicationPayload.getGuid())
                .orElseThrow(() -> new ApplicationNotFoundException("No Application exists with guid - " + applicationPayload.getGuid()));

        application.setName(applicationPayload.getApplicationName());
        application.setEnvironmentVariables(applicationPayload.getEnvironmentVariablesList());
        application.setPort(applicationPayload.getPort());

        return applicationMapper.map(applicationRepository.save(application));
    }

    private ApplicationResponse findAppByGuid(String guid) {
        return applicationRepository.findByGuid(guid)
                .map(applicationMapper::map)
                .orElseThrow(() -> new RapidDeployException(NO_APP_FOUND_GUID + guid));
    }

    public String startApplicationContainer(String guid) {
        Application application = applicationRepository.findByGuid(guid)
                .orElseThrow(() -> new RapidDeployException("No Application found " +
                        "with guid - " + guid));
        String imageId = application.getImageId();
        List<EnvironmentVariables> environmentVariables = application.getEnvironmentVariables();
        String name = application.getName();

        Pair<String, String> container = dockerContainerService.run(DockerContainerPayload.builder()
                .imageId(imageId)
                .environmentVariables(environmentVariables)
                .name(RAPID_DEPLOY_SERVICE_PREFIX + name)
                .port(application.getPort())
                .exposedPort(application.getPort())
                .volumeList(Collections.singletonList(new Volume(name,
                        "/tmp/cache/.m2", MountType.VOLUME)))
                .environmentVariables(application.getEnvironmentVariables())
                .build());
        nginxService.createConfigFile(RAPID_DEPLOY_SERVICE_PREFIX + name, application.getPort());
        nginxService.start();
        application.setContainerId(container.getFirst());
        applicationRepository.save(application);
        return container.getFirst();
    }

    public void stopApplicationContainer(String guid) {
        Application application = applicationRepository.findByGuid(guid)
                .orElseThrow(() -> new RapidDeployException("No Application found " +
                        "with guid - " + guid));
        dockerContainerService.stop(application.getContainerId());
    }

    public String getContainerId(String guid) {
        Application application = applicationRepository.findByGuid(guid)
                .orElseThrow(() -> new RapidDeployException("Cannot find app by guid - " + guid));
        return application.getContainerId();
    }

    public String getContainerIdByAppName(String appName) {
        Application application = applicationRepository.findByName(appName)
                .orElseThrow(() -> new RapidDeployException("Cannot find app by name - " + appName));
        return application.getContainerId();
    }
}
