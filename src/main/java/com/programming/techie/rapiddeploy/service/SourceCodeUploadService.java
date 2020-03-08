package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.events.FileUploaded;
import com.programming.techie.rapiddeploy.exceptions.RapidDeployException;
import com.programming.techie.rapiddeploy.model.Application;
import com.programming.techie.rapiddeploy.repository.ApplicationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class SourceCodeUploadService {
    private final ApplicationRepository applicationRepository;
    private final FileStorageService fileStorageService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public String upload(MultipartFile file, String appName) {
        Application application = applicationRepository.findByName(appName)
                .orElseThrow(() -> new RapidDeployException("Cannot Find app by name - " + appName));

        String fullFileName = fileStorageService.upload(file);
        String fileName = Objects.requireNonNull(file.getOriginalFilename()).replace(".zip", "");
        applicationEventPublisher.publishEvent(new FileUploaded(application.getGuid(), fullFileName, fileName));

        Application updatedApp = applicationRepository.findByName(appName)
                .orElseThrow(() -> new RapidDeployException("Cannot find application " +
                        "by name - " + appName));
        return updatedApp.getContainerId();
    }


}