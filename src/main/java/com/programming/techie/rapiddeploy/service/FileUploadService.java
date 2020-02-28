package com.programming.techie.rapiddeploy.service;

import com.programming.techie.rapiddeploy.events.FileUploaded;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
public class FileUploadService {
    private final FileStorageService fileStorageService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void upload(MultipartFile file, String appName) {
        fileStorageService.upload(file);
        applicationEventPublisher.publishEvent(new FileUploaded(appName));
    }
}
