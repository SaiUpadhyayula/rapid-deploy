package com.programming.techie.rapiddeploy.service.application;

import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.mapper.ApplicationMapper;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.model.DockerContainerPayload;
import com.programming.techie.rapiddeploy.model.EnvironmentVariables;
import com.programming.techie.rapiddeploy.payload.ApplicationPayload;
import com.programming.techie.rapiddeploy.payload.ApplicationResponse;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import com.programming.techie.rapiddeploy.service.docker.DockerContainerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationService {

    public static final String NO_APP_FOUND_GUID = "No Application found with guid - ";
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final DockerContainerService dockerContainerService;

    public ApplicationResponse create(ApplicationPayload applicationPayload) {
        String applicationName = applicationPayload.getApplicationName();
        Application application = Application.builder()
                .name(applicationName)
                .guid(UUID.randomUUID().toString())
                .environmentVariables(Collections.emptyList())
                .build();
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

    public void update(ApplicationPayload applicationPayload) {

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
                .name("srv-" + name)
                .port(8080)
                .exposedPort(8080)
                .environmentVariables(application.getEnvironmentVariables())
                .build());
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
}
