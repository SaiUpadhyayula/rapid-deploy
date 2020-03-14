package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.mapper.ApplicationMapper;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.payload.ApplicationPayload;
import com.programming.techie.rapiddeploy.payload.ApplicationResponse;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

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
        return applicationRepository.findByGuid(guid)
                .map(applicationMapper::map)
                .orElseThrow(() -> new RapidDeployException("No Application found " +
                        "with guid - " + guid));
    }

    public void delete(String guid) {
        applicationRepository.deleteByGuid(guid);
    }

    public void update(ApplicationPayload applicationPayload) {

    }
}
