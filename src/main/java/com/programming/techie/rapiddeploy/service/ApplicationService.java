package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.mapper.ApplicationMapper;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.model.EnvironmentVariables;
import com.programming.techie.rapiddeploy.payload.ApplicationPayload;
import com.programming.techie.rapiddeploy.payload.ApplicationResponse;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

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
        Pair<String, String> container = dockerContainerService.run(imageId, environmentVariables, name);
        return container.getFirst();
    }

    public void stopApplicationContainer(String guid) {
        Application application = applicationRepository.findByGuid(guid)
                .orElseThrow(() -> new RapidDeployException("No Application found " +
                        "with guid - " + guid));
        dockerContainerService.stop(application.getContainerId());
    }


}
